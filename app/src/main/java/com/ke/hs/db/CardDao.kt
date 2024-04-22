package com.ke.hs.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ke.hs.entity.Card

@Dao
interface CardDao {

    @Query("select * from card")
    suspend fun getAll(): List<Card>

    @Insert
    suspend fun insert(list: List<Card>)

    @Query("delete from card")
    suspend fun deleteAll()

    @Query("select COUNT(id) from card")
    suspend fun getCount(): Int
}