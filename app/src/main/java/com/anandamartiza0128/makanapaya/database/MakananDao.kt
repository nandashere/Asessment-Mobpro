package com.anandamartiza0128.makanapaya.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.anandamartiza0128.makanapaya.model.Makanan
import kotlinx.coroutines.flow.Flow

@Dao
interface MakananDao {

    @Insert
    suspend fun insert(makanan: Makanan)

    @Update
    suspend fun update(makanan: Makanan)

    @Delete
    suspend fun delete(makanan: Makanan)

    @Query("SELECT * FROM makanan ORDER BY nama DESC")
    fun getMakanan(): Flow<List<Makanan>>

    @Query("SELECT * FROM makanan WHERE id = :id")
    suspend fun getMakananById(id: Long): Makanan?
}