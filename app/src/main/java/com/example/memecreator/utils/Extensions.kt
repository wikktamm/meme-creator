package com.example.memecreator.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSnackBar(text: Int, root: View){
    Snackbar.make(root, getString(text), Snackbar.LENGTH_SHORT).show()

}