package com.dicoding.aristiyo.githubusers.repository.localfavorite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.aristiyo.githubusers.models.UserDetails

@Dao
interface Dao {

    @Insert
    fun insert(userDetails: UserDetails)

    @Query("SELECT count(*) FROM userdetails WHERE userdetails.login = :username")
    fun check(username: String): Int

    @Delete
    fun delete(userDetails: UserDetails)

    @Query("SELECT * FROM userdetails ORDER BY id ASC")
    fun getAll(): LiveData<List<UserDetails>>
}