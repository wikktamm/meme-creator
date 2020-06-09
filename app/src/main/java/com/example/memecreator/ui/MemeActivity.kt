package com.example.memecreator.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.memecreator.R
import com.example.memecreator.db.api.MemeRepository
import com.example.memecreator.viewmodels.MemeViewModel
import com.example.memecreator.viewmodels.MemeViewModelFactory
import kotlinx.android.synthetic.main.activity_meme.*

class MemeActivity : AppCompatActivity() {
    lateinit var viewModel: MemeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)
        bottomNavView.setupWithNavController(navHost.findNavController())
        val repo = MemeRepository() //todo add db for room
        val factory = MemeViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(MemeViewModel::class.java)
    }
}