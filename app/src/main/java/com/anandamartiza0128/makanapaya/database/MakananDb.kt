package com.anandamartiza0128.makanapaya.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anandamartiza0128.makanapaya.model.Makanan

@Database(entities = [Makanan::class], version = 2, exportSchema = false)
abstract class MakananDb : RoomDatabase() {

    abstract val dao: MakananDao

    companion object {

        @Volatile
        private var INSTANCE: MakananDb? = null

        fun getInstance(context: Context): MakananDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MakananDb::class.java,
                        "makanan.db"
                    )
                        // Tambahkan baris ini untuk penanganan migrasi yang merusak selama pengembangan
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
