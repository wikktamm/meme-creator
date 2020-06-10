package com.example.memecreator.ui

import android.os.Bundle
import android.view.View
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
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import kotlinx.android.synthetic.main.fragment_meme_editor.*


class MemeEditorFragment : Fragment(R.layout.fragment_meme_editor)
{
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
    private fun setupPhotoEditorListeners(){
        menuItemText.setOnClickListener {
            val textEditorDialogFragment =
                TextEditorDialogFragment.show(activity as MemeActivity)
            textEditorDialogFragment.setOnTextEditorListener(object : TextEditor {
                override fun onDone(inputText: String?, colorCode: Int) {
                    val styleBuilder = TextStyleBuilder()
                    styleBuilder.withTextColor(colorCode)
                    photoEditor.addText(inputText, styleBuilder)
                    //txtCurrentTool.setText(R.string.label_text)
                }
            })
        }
    }
}