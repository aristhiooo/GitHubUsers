package com.dicoding.aristiyo.githubusers.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory

class ViewModelsFactory private constructor(private val application: Application) :
    NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelsFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelsFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelsFactory::class.java) {
                    INSTANCE = ViewModelsFactory(application)
                }
            }
            return INSTANCE as ViewModelsFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ViewModelsWithContext::class.java) -> {
                ViewModelsWithContext(application) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}