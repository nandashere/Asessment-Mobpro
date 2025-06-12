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

    fun deleteMakanan(makanan: Makanan) {
        viewModelScope.launch {
            dao.delete(makanan)
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

                Log.d("API_DEBUG", "Retrieved ID Token (first 10 chars): ${idToken.take(10)}...")
                Log.d("API_DEBUG", "Full ID Token length: ${idToken.length}")
                Log.d("API_DEBUG", "Sending Authorization Header: '$authHeader'")
                Log.d("API_DEBUG", "Sending user_id Header: '$userIdValue'")

                // Panggil API
                val response = MakananApi.service.getMakananList(authHeader, userIdValue)
                _makananListFromApi.value = response.data // <-- AMBIL DARI PROPERTI 'data'
                _status.value = MakananApiStatus.DONE

            } catch (e: IOException) {
                _status.value = MakananApiStatus.ERROR
                _errorMessage.value = "Koneksi internet bermasalah. Coba lagi nanti."
                _makananListFromApi.value = emptyList()
                e.printStackTrace()
            } catch (e: HttpException) {
                _status.value = MakananApiStatus.ERROR
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("API_ERROR", "HTTP Exception Code: ${e.code()}, Message: ${e.message()}, Error Body: $errorBody")
                if (e.code() == 401 || e.code() == 403) {
                    _errorMessage.value = "Akses ditolak. Silakan login kembali."
                } else {
                    // Bisa juga cek errorBody untuk pesan spesifik dari server
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
        return makananList.value
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
