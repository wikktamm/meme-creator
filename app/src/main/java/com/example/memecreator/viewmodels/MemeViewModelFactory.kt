package com.example.memecreator.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memecreator.db.api.MemeRepository

class MemeViewModelFactory(val repo: MemeRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemeViewModel(repo) as T
    }
}