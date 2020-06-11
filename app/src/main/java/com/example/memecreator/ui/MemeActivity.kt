package com.example.memecreator.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.memecreator.R
import com.example.memecreator.db.local.MemesDatabase
import com.example.memecreator.repositories.MemeRepository
import com.example.memecreator.viewmodels.MemeViewModel
import com.example.memecreator.viewmodels.MemeViewModelFactory
import kotlinx.android.synthetic.main.activity_meme.*

class MemeActivity : AppCompatActivity() {
    lateinit var viewModel: MemeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)
        bottomNavView.setupWithNavController(navHost.findNavController())
        val database = MemesDatabase(this)
        val repo =
            MemeRepository(database)
        val factory = MemeViewModelFactory(repo, application)
        viewModel = ViewModelProvider(this, factory).get(MemeViewModel::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}