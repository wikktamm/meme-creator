package com.example.memecreator.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.api.load
import com.example.memecreator.R
import com.example.memecreator.adapters.MemeLocalAdapter
import com.example.memecreator.db.models.meme.MemeLocal
import com.example.memecreator.viewmodels.MemeViewModel
import kotlinx.android.synthetic.main.fragment_saved_memes.*
import kotlinx.android.synthetic.main.row_meme.view.*

class SavedMemesFragment : Fragment(R.layout.fragment_saved_memes) {
    lateinit var adapter : MemeLocalAdapter
    lateinit var viewModel : MemeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MemeActivity).viewModel
        setupRecyclerView()
        viewModel.getAllSavedMemes().observe(
            viewLifecycleOwner,
            Observer {
                adapter.diffUtil.submitList(it.toList())
                hideProgressBar()
            }
        )
        viewModel.getAllSavedMemes()
        showProgressBar()
    }

    private fun setupRecyclerView() {
        adapter = MemeLocalAdapter()
        rvSavedMemes.also {
            it.adapter = adapter
            it.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        adapter.setOnMemeClickListener {
            val meme = it as MemeLocal
            val view = layoutInflater.inflate(R.layout.row_meme, null)
            view.ivMeme.load(meme.uri)
            AlertDialog.Builder(requireActivity())
                .setView(view)
                .setNeutralButton("Dismiss"){dialogInterface, i ->  }
                .setPositiveButton("Share") { _, _ ->
                    Toast.makeText(requireContext(), "Clicked on ACCEPT", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Delete") { _, _ ->
                    Toast.makeText(requireContext(), "Clicked on DECLINE", Toast.LENGTH_SHORT).show()
                }
                .create().show()
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
}