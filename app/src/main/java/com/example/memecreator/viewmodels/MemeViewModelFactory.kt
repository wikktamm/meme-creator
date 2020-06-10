package com.example.memecreator.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memecreator.repositories.MemeRepository

class MemeViewModelFactory(val repo: MemeRepository, val application : Application) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemeViewModel(repo, application) as T
    }
}