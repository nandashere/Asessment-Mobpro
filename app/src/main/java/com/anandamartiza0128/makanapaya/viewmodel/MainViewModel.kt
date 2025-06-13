package com.anandamartiza0128.makanapaya.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anandamartiza0128.makanapaya.database.MakananDao
import com.anandamartiza0128.makanapaya.model.Makanan
import com.anandamartiza0128.makanapaya.network.MakananApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import com.anandamartiza0128.makanapaya.network.UserDataStore
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

enum class MakananApiStatus { LOADING, ERROR, DONE }

class MainViewModel(
    private val dao: MakananDao,
    private val userDataStore: UserDataStore
) : ViewModel() {

    // Menyimpan semua makanan dari database dalam bentuk StateFlow
    val makananList: StateFlow<List<Makanan>> = dao.getMakanan()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // State untuk status pengambilan data dari API
    private val _status = MutableStateFlow(MakananApiStatus.LOADING)
    val status: StateFlow<MakananApiStatus> = _status.asStateFlow()

    // State untuk data makanan yang diambil dari API
    private val _makananListFromApi = MutableStateFlow<List<Makanan>>(emptyList())
    val makananListFromApi: StateFlow<List<Makanan>> = _makananListFromApi.asStateFlow()

    // State untuk pesan error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        getMakananFromApi()
    }


    // ✅ Tambahkan fungsi insert data ke database
    fun addMakanan(makanan: Makanan) {
        viewModelScope.launch {
            dao.insert(makanan)
        }
    }

    fun addMakananToApi(makanan: Makanan, imageFile: File?) {
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = MakananApiStatus.LOADING
            _errorMessage.value = null

            try {
                val user = userDataStore.userFlow.first()
                val idToken = user.idToken
                val authHeader = "Bearer $idToken"
                val userIdValue = user.email

                Log.d("API_DEBUG", "Mengirim Header Authorization: '$authHeader'")
                Log.d("API_DEBUG", "Mengirim Header user_id: '$userIdValue'")

                val namaPart = makanan.nama.toRequestBody("text/plain".toMediaTypeOrNull())
                val jenisPart = makanan.jenis.toRequestBody("text/plain".toMediaTypeOrNull())
                val rasaPart = makanan.rasa.toRequestBody("text/plain".toMediaTypeOrNull())
                val tingkatPedasPart = makanan.tingkatPedas.toRequestBody("text/plain".toMediaTypeOrNull())
                val teksturPart = makanan.tekstur.toRequestBody("text/plain".toMediaTypeOrNull())

                var imageMultipart: MultipartBody.Part? = null
                if (imageFile != null) {
                    val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    imageMultipart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
                }

                val result = MakananApi.service.postMakanan(
                    authHeader,
                    userIdValue,
                    namaPart,
                    jenisPart,
                    rasaPart,
                    tingkatPedasPart,
                    teksturPart,
                    imageMultipart
                )

                getMakananFromApi()
                _status.value = MakananApiStatus.DONE

            } catch (e: IOException) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Koneksi internet bermasalah. Coba lagi nanti."
                Log.e("API_ERROR", "IOException: ${e.message}", e)
            } catch (e: HttpException) {
                _status.value = MakananApiStatus.ERROR
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("API_ERROR", "Kode Pengecualian HTTP: ${e.code()}, Pesan: ${e.message()}, Body Error: $errorBody", e)
                if (e.code() == 401 || e.code() == 403) {
                    _errorMessage.value = "Akses ditolak. Silakan login kembali."
                } else {
                    _errorMessage.value = "Gagal menambahkan data ke server. Kode Error: ${e.code()}. Detail: ${errorBody ?: "Tidak ada detail error."}"
                }
            } catch (e: Exception) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Terjadi kesalahan tidak terduga: ${e.localizedMessage}"
                Log.e("API_ERROR", "Pengecualian Umum: ${e.message}", e)
            }
        }
    }

    fun deleteMakananFromApi(makananId: Int) { // Mengganti nama parameter dari 'id' menjadi 'makananId' untuk kejelasan
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = MakananApiStatus.LOADING
            _errorMessage.value = null

            try {
                val user = userDataStore.userFlow.first()
                val idToken = user.idToken
                val authHeader = "Bearer $idToken"
                val userIdValue = user.email

                val response = MakananApi.service.deleteMakanan(makananId, authHeader, userIdValue) // Menggunakan makananId di sini
                if (response.isSuccessful) {
                    Log.d("Delete", "Makanan deleted successfully")
                    getMakananFromApi() // Refresh list setelah hapus
                } else {
                    Log.e("Delete", "Error deleting makanan: ${response.code()}")
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Gagal menghapus data. Kode Error: ${response.code()}. Detail: ${errorBody ?: "Tidak ada detail error."}"
                }
                _status.value = MakananApiStatus.DONE
            } catch (e: IOException) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Koneksi internet bermasalah. Coba lagi nanti."
                Log.e("API_ERROR", "IOException (Delete): ${e.message}", e)
            } catch (e: HttpException) {
                _status.value = MakananApiStatus.ERROR
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("API_ERROR", "HTTP Exception (Delete): ${e.code()}, Message: ${e.message()}, Body Error: $errorBody", e)
                if (e.code() == 401 || e.code() == 403) {
                    _errorMessage.value = "Akses ditolak. Silakan login kembali."
                } else {
                    _errorMessage.value = "Gagal menghapus data di server. Kode Error: ${e.code()}. Detail: ${errorBody ?: "Tidak ada detail error."}"
                }
            } catch (e: Exception) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Terjadi kesalahan tidak terduga: ${e.localizedMessage}"
                Log.e("API_ERROR", "Pengecualian Umum (Delete): ${e.message}", e)
            }
        }
    }

    fun updateMakananInApi(makanan: Makanan, imageFile: File? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = MakananApiStatus.LOADING
            _errorMessage.value = null

            try {
                val user = userDataStore.userFlow.first()
                val idToken = user.idToken
                val authHeader = "Bearer $idToken"
                val userIdValue = user.email

                Log.d("API_DEBUG", "Mengirim Header Authorization: '$authHeader'")
                Log.d("API_DEBUG", "Mengirim Header user_id: '$userIdValue'")

                val namaPart = makanan.nama.toRequestBody("text/plain".toMediaTypeOrNull())
                val jenisPart = makanan.jenis.toRequestBody("text/plain".toMediaTypeOrNull())
                val rasaPart = makanan.rasa.toRequestBody("text/plain".toMediaTypeOrNull())
                val tingkatPedasPart = makanan.tingkatPedas.toRequestBody("text/plain".toMediaTypeOrNull())
                val teksturPart = makanan.tekstur.toRequestBody("text/plain".toMediaTypeOrNull())

                var imageMultipart: MultipartBody.Part? = null
                if (imageFile != null) {
                    val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    imageMultipart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
                }

                // Panggil fungsi updateMakanan dari MakananApi.service
                val result = MakananApi.service.updateMakanan(
                    makanan.id, // ID makanan yang akan diupdate
                    authHeader,
                    userIdValue,
                    namaPart,
                    jenisPart,
                    rasaPart,
                    tingkatPedasPart,
                    teksturPart,
                    imageMultipart // Teruskan gambar multipart (bisa null)
                )

                getMakananFromApi() // Ambil ulang untuk memperbarui daftar dengan item baru
                _status.value = MakananApiStatus.DONE

            } catch (e: IOException) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Koneksi internet bermasalah. Gagal mengupdate data."
                Log.e("API_ERROR", "IOException (Update): ${e.message}", e)
            } catch (e: HttpException) {
                _status.value = MakananApiStatus.ERROR
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("API_ERROR", "HTTP Exception (Update): ${e.code()}, Message: ${e.message()}, Body Error: $errorBody", e)
                if (e.code() == 401 || e.code() == 403) {
                    _errorMessage.value = "Akses ditolak. Silakan login kembali."
                } else {
                    _errorMessage.value = "Gagal mengupdate data di server. Kode Error: ${e.code()}. Detail: ${errorBody ?: "Tidak ada detail error."}"
                }
            } catch (e: Exception) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Terjadi kesalahan tidak terduga saat mengupdate: ${e.localizedMessage}"
                Log.e("API_ERROR", "Pengecualian Umum (Update): ${e.message}", e)
            }
        }
    }

    fun getMakananFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = MakananApiStatus.LOADING
            _errorMessage.value = null

            try {
                val user = userDataStore.userFlow.first()
                val idToken = user.idToken
                val authHeader = "Bearer $idToken"
                val userIdValue = user.email

                Log.d("API_DEBUG", "Mengambil Token ID (10 karakter pertama): ${idToken.take(10)}...")
                Log.d("API_DEBUG", "Panjang Token ID Penuh: ${idToken.length}")
                Log.d("API_DEBUG", "Mengirim Header Authorization: '$authHeader'")
                Log.d("API_DEBUG", "Mengirim Header user_id: '$userIdValue'")

                val response = MakananApi.service.getMakananList(authHeader, userIdValue)
                _makananListFromApi.value = response.data
                _status.value = MakananApiStatus.DONE

            } catch (e: IOException) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Koneksi internet bermasalah. Coba lagi nanti."
                _makananListFromApi.value = emptyList()
                e.printStackTrace()
            } catch (e: HttpException) {
                _status.value = MakananApiStatus.ERROR
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("API_ERROR", "Kode Pengecualian HTTP: ${e.code()}, Pesan: ${e.message()}, Body Error: $errorBody")
                if (e.code() == 401 || e.code() == 403) {
                    _errorMessage.value = "Akses ditolak. Silakan login kembali."
                } else {
                    _errorMessage.value = "Gagal mengambil data dari server. Kode Error: ${e.code()}. Detail: ${errorBody ?: "Tidak ada detail error."}"
                }
                _makananListFromApi.value = emptyList()
                e.printStackTrace()
            } catch (e: Exception) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Terjadi kesalahan tidak terduga: ${e.localizedMessage}"
                _makananListFromApi.value = emptyList()
                e.printStackTrace()
            }
        }
    }

    // Fungsi cari rekomendasi
    fun cariRekomendasi(
        jenis: String,
        rasa: String,
        tingkatPedas: String,
        tekstur: String
    ): List<Makanan> {
        // Sekarang ini harus mencari dari _makananListFromApi juga
        return _makananListFromApi.value
            .map { makanan ->
                var skor = 0
                if (makanan.jenis.equals(jenis, ignoreCase = true)) skor++
                if (makanan.rasa.equals(rasa, ignoreCase = true)) skor++
                if (makanan.tingkatPedas.equals(tingkatPedas, ignoreCase = true)) skor++
                if (makanan.tekstur.equals(tekstur, ignoreCase = true)) skor++
                Pair(makanan, skor)
            }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .map { it.first }
    }
}
