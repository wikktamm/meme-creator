package com.example.memecreator.utils

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import com.example.memecreator.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(text: Int, root: View){
    Snackbar.make(root, getString(text), Snackbar.LENGTH_SHORT).show()
}

fun Fragment.performShareOperation(savedMemeUri: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(savedMemeUri))
    intent.type = Constants.INTENT_SEND_IMAGE_TYPE
    startActivity(Intent.createChooser(intent, getString(R.string.share_meme_via)))
}