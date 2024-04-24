package com.example.chronicledisease.presentation.commonpack

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar

fun View.snackView(message: Any?){
    Snackbar.make(this,"$message",Snackbar.LENGTH_SHORT).show()
}
fun Activity.showToast(message: Any?){
    Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
}
fun spanned(message: String)=HtmlCompat.fromHtml(message,HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS)

fun logview(message: Any?)= Log.i("Application test","$message")