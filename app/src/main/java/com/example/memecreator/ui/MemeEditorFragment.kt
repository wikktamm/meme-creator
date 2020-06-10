package com.example.memecreator.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.memecreator.R
import com.example.memecreator.db.models.meme.Meme
import com.example.memecreator.viewmodels.MemeViewModel
import kotlinx.android.synthetic.main.row_meme.*

class MemeEditorFragment : Fragment(R.layout.fragment_meme_editor) {
    lateinit var viewModel: MemeViewModel
    val args:MemeEditorFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MemeActivity).viewModel

        viewModel.chosenMeme.observe(viewLifecycleOwner, Observer {
            ivMeme.load(it.url)

        })
        args.meme?.let {
            viewModel.rememberChosenMeme(it)
        }
    }
}