package com.example.memecreator.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.memecreator.R
import com.example.memecreator.tools_editing.TextEditorDialogFragment
import com.example.memecreator.tools_editing.TextEditorDialogFragment.TextEditor
import com.example.memecreator.utils.Constants.READ_WRITE_STORAGE
import com.example.memecreator.utils.Resource
import com.example.memecreator.utils.showSnackBar
import com.example.memecreator.viewmodels.MemeViewModel
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import kotlinx.android.synthetic.main.fragment_meme_editor.*

class MemeEditorFragment : Fragment(R.layout.fragment_meme_editor) {
    private lateinit var viewModel: MemeViewModel
    lateinit var photoEditor: PhotoEditor
    private val args: MemeEditorFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MemeActivity).viewModel
        viewModel.chosenMeme.observe(viewLifecycleOwner, Observer {
            photoEditorView.source.load(it.url)
        })

        viewModel.savingMemeResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgressBar()
                    showSnackBar(R.string.meme_saved_succesfully, rootView)
                    viewModel.savingMemeResult.postValue(Resource.None())
                }
                is Resource.Error -> {
                    hideProgressBar()
                    showSnackBar(R.string.meme_failed_to_save, rootView)
                    viewModel.savingMemeResult.postValue(Resource.None())
                }
                is Resource.Loading -> showProgressBar()
                is Resource.None -> hideProgressBar()
            }
        })
        args.meme?.let {
            viewModel.rememberChosenMeme(it)
        }
        setupPhotoEditor()
        setupPhotoEditorListeners()
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun setupPhotoEditor() {
        val mTextRobotoTf = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
        photoEditor = PhotoEditor.Builder(activity, photoEditorView)
            .setPinchTextScalable(true)
            .setDefaultTextTypeface(mTextRobotoTf)
            .build()
    }

    private fun setupPhotoEditorListeners() {
        menuItemText.setOnClickListener {
            val textEditorDialogFragment =
                TextEditorDialogFragment.show(activity as MemeActivity)
            textEditorDialogFragment.setOnTextEditorListener(object : TextEditor {
                override fun onDone(inputText: String?, colorCode: Int) {
                    val styleBuilder = TextStyleBuilder()
                    styleBuilder.withTextColor(colorCode)
                    photoEditor.addText(inputText, styleBuilder)
                }
            })
        }
        menuItemRedo.setOnClickListener {
            photoEditor.redo()
        }
        menuItemUndo.setOnClickListener {
            photoEditor.undo()
        }
        menuItemSave.setOnClickListener {
            saveImage()
        }
        menuItemShare.setOnClickListener {
            //todo
        }
    }

    @SuppressLint("MissingPermission")
    private fun saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            viewModel.saveMemeExternally(photoEditor, photoEditorView)
        }
    }

    private fun requestPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isGranted =
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            if (!isGranted) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(permission),
                    READ_WRITE_STORAGE
                )
            }
            return isGranted
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_WRITE_STORAGE -> isPermissionGranted(
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            )
        }
    }

    private fun isPermissionGranted(permissionGranted: Boolean) {
        if (permissionGranted) saveImage()
    }
}

