package com.example.memecreator.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import com.example.memecreator.R
import com.example.memecreator.viewmodels.MemeViewModel
import kotlinx.android.synthetic.main.fragment_saved_memes.*

class SavedMemesFragment : Fragment(R.layout.fragment_saved_memes) {
    private lateinit var viewModel: MemeViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MemeActivity).viewModel
        viewModel.getAllSavedMemes().observe(viewLifecycleOwner, Observer {
            Log.d("123", it[it.size-1].uri)
            imageView.load(it[it.size-1].uri)
        })
    }
}