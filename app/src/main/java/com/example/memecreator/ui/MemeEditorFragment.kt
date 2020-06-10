package com.example.memecreator.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import com.example.memecreator.viewmodels.MemeViewModel
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import ja.burhanrashid52.photoeditor.SaveSettings
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import kotlinx.android.synthetic.main.fragment_meme_editor.*
import java.io.File


class MemeEditorFragment : Fragment(R.layout.fragment_meme_editor) {
    lateinit var viewModel: MemeViewModel
    lateinit var photoEditor: PhotoEditor
    val args: MemeEditorFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = (activity as MemeActivity).viewModel

        viewModel.chosenMeme.observe(viewLifecycleOwner, Observer {
            photoEditorView.source.load(it.url)

        })
        args.meme?.let {
            viewModel.rememberChosenMeme(it)
        }
        setupPhotoEditor()
        setupPhotoEditorListeners()
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
    }

    var mSaveImageUri: Uri? = null

    @SuppressLint("MissingPermission")
    private fun saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            showLoading("Saving...")
//            val path =
//                File(requireContext().filesDir, "Meme_creator" + File.separator + "Images")
//            if (!path.exists()) {
//                path.mkdirs()
//            }
//            val file = File(path, System.currentTimeMillis().toString() + ".jpeg")
            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + ""
                        + System.currentTimeMillis() + ".png")
            try {
                val saveSettings = SaveSettings.Builder()
                    .setClearViewsEnabled(true)
                    .setTransparencyEnabled(true)
                    .build()
                photoEditor.saveAsFile(
                    file.absolutePath,
                    saveSettings,
                    object : OnSaveListener {
                        override fun onSuccess(imagePath: String) {
//                            hideLoading()
//                            showSnackbar("Image Saved Successfully")
                            Log.d("dupa", "brawo" + imagePath)

                            mSaveImageUri = Uri.fromFile(File(imagePath))
                            photoEditorView.source.setImageURI(mSaveImageUri)
                            context!!.sendBroadcast(
                                Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.fromFile(file)
                                )
                            )

                        }

                        override fun onFailure(exception: Exception) {
//                            hideLoading()
                            Log.d("dupa", exception.toString())

//                            showSnackbar("Failed to save Image")
                        }
                    })
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.d("dupa", "nie wyszlo")
                Log.d("dupa2", e.toString())

//                hideLoading()
//                showSnackbar(e.getMessage())
            }
        }
    }

    val READ_WRITE_STORAGE = 52
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
                grantResults[0] == PackageManager.PERMISSION_GRANTED,
                permissions[0]
            )
        }
    }

    private fun isPermissionGranted(b: Boolean, s: String?) {
        if (b) saveImage()
    }

}

