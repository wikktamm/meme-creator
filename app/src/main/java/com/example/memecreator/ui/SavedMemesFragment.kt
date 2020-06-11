package com.example.memecreator.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.memecreator.R
import com.example.memecreator.adapters.MemeLocalAdapter
import com.example.memecreator.viewmodels.MemeViewModel
import kotlinx.android.synthetic.main.fragment_saved_memes.*

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
            //todo
            //val bundle = Bundle()
            //bundle.putSerializable("meme", it)
           //// findNavController().navigate(R.id.action_memesListFragment_to_memeEditorFragment, bundle)
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
}