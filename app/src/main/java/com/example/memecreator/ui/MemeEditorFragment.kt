package com.example.memecreator.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.memecreator.R
import com.example.memecreator.db.models.meme.MemesResponse
import com.example.memecreator.utils.Resource
import kotlinx.android.synthetic.main.fragment_meme_editor.*

class MemeEditorFragment : Fragment(R.layout.fragment_meme_editor) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel = (activity as MemeActivity).viewModel
        viewModel.memesData.observe(
            viewLifecycleOwner,
            Observer { resource: Resource<MemesResponse>? ->
                when (resource) {
                    is Resource.Success -> {
                        hideProgressBar()
                        textView.text = resource.body.toString()
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        //todo
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        viewModel.getAllMemes()
    }


    private fun showProgressBar() {

    }
    private fun hideProgressBar() {

    }
}