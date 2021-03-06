package com.example.memecreator.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.api.load
import com.example.memecreator.R
import com.example.memecreator.adapters.MemeLocalAdapter
import com.example.memecreator.db.models.meme.MemeLocal
import com.example.memecreator.db.models.meme.MemeTemplate
import com.example.memecreator.utils.performShareOperation
import com.example.memecreator.viewmodels.MemeViewModel
import kotlinx.android.synthetic.main.fragment_saved_memes.*
import kotlinx.android.synthetic.main.row_meme.view.*

class SavedMemesFragment : Fragment(R.layout.fragment_saved_memes) {
    private lateinit var adapter: MemeLocalAdapter
    private lateinit var viewModel: MemeViewModel

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
            showDialog(it)
        }
    }

    private fun showDialog(meme: MemeTemplate) {
        val memeLocal = meme as MemeLocal
        val view = layoutInflater.inflate(R.layout.row_meme, null)
        view.ivMeme.load(memeLocal.uri)
        AlertDialog.Builder(requireActivity())
            .setView(view)
            .setNeutralButton(getString(R.string.close)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .setPositiveButton(getString(R.string.share)) { _, _ ->
                performShareOperation(memeLocal.uri)
            }
            .setNegativeButton(getString(R.string.delete)) { _, _ ->
                askAboutDeleting { viewModel.deleteMeme(memeLocal) }
            }
            .create().show()
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun askAboutDeleting(onYesClickedCallback: (()->Unit)) {
        AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.prompt_sure_about_deleting))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                onYesClickedCallback.invoke()
            }
            .setNegativeButton(getString(R.string.no)) { _, _ ->
            }
            .create().show()
    }
}