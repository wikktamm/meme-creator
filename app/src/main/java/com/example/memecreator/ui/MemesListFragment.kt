package com.example.memecreator.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.memecreator.R
import com.example.memecreator.adapters.MemeAdapter
import com.example.memecreator.db.models.meme.Meme
import com.example.memecreator.db.models.meme.MemesResponse
import com.example.memecreator.utils.Constants.ARGS_KEY_MEME
import com.example.memecreator.utils.Resource
import com.example.memecreator.viewmodels.MemeViewModel
import kotlinx.android.synthetic.main.fragment_memes_list.*

class MemesListFragment : Fragment(R.layout.fragment_memes_list) {
    private lateinit var adapter : MemeAdapter
    private lateinit var viewModel : MemeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MemeActivity).viewModel
        setupRecyclerView()
        viewModel.memesData.observe(
            viewLifecycleOwner,
            Observer { resource: Resource<MemesResponse>? ->
                when (resource) {
                    is Resource.Success -> {
                        hideProgressBar()
                        adapter.diffUtil.submitList(resource.body?.data?.memes)

                    }
                    is Resource.Error -> {
                        hideProgressBar()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        viewModel.getAllMemes()
    }

    private fun setupRecyclerView() {
        adapter = MemeAdapter()
        rvApiMemes.also {
            it.adapter = adapter
            it.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }
        adapter.setOnMemeClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ARGS_KEY_MEME, it as Meme)
            findNavController().navigate(R.id.action_memesListFragment_to_memeEditorFragment, bundle)
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
}