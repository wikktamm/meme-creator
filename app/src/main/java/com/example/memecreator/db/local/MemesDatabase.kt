package com.example.memecreator.db.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.memecreator.db.models.meme.MemeLocal

@Database(entities = [MemeLocal::class], version = 1)
abstract class MemesDatabase : RoomDatabase() {
    abstract fun getMemesDao(): MemesDao

    companion object {
        var instance: MemesDatabase? = null
        val LOCK = Any()
        operator fun invoke(context: Context) =
            instance ?: synchronized(LOCK) {
                instance ?: createDatabase(context).also { instance = it }
            }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MemesDatabase::class.java,
            "memes_db.db"
        ).build()
    }
}