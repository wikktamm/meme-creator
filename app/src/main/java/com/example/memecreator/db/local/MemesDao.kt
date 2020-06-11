package com.example.memecreator.db.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.memecreator.db.models.meme.MemeLocal

@Dao
interface MemesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMeme(meme:MemeLocal)
    @Query("SELECT * FROM memes")
    fun getSavedMemes(): LiveData<List<MemeLocal>>
    @Delete
    suspend fun deleteMeme(meme: MemeLocal)
}