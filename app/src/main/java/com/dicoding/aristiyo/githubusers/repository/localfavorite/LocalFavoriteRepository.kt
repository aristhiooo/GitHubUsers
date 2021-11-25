package com.dicoding.aristiyo.githubusers.repository.localfavorite

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.aristiyo.githubusers.models.UserDetails
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalFavoriteRepository(application: Application) {

    private val mDao: Dao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val database = FavoriteRoomDatabase.getDatabase(application)
        mDao = database.dao()
    }

    fun getAllFavorites(): LiveData<List<UserDetails>> = mDao.getAll()

    fun insertFavorite(userDetails: UserDetails) {
        executorService.execute {
            mDao.insert(userDetails)
        }
    }

    fun deleteFavorite(userDetails: UserDetails) {
        executorService.execute {
            mDao.delete(userDetails)
        }
    }

    fun check(username: String) = mDao.check(username)
}