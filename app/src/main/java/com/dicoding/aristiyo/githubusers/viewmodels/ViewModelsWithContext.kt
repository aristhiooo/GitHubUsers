package com.dicoding.aristiyo.githubusers.viewmodels

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.aristiyo.githubusers.models.UserDetails
import com.dicoding.aristiyo.githubusers.repository.localfavorite.LocalFavoriteRepository
import com.dicoding.aristiyo.githubusers.settings.SettingPreferences
import kotlinx.coroutines.launch

class ViewModelsWithContext(application: Application) : ViewModel() {

    private val localFavoriteRepository: LocalFavoriteRepository =
        LocalFavoriteRepository(application)

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val pref = SettingPreferences.getInstance(application.dataStore)

    fun getAllFavorites(): LiveData<List<UserDetails>> = localFavoriteRepository.getAllFavorites()

    fun insert(userDetails: UserDetails) = localFavoriteRepository.insertFavorite(userDetails)

    fun check(username: String) = localFavoriteRepository.check(username)

    fun delete(userDetails: UserDetails) = localFavoriteRepository.deleteFavorite(userDetails)

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}