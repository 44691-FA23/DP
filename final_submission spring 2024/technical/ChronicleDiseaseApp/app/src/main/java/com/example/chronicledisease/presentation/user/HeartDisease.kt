package com.example.chronicledisease.presentation.user

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.example.chronicledisease.R

class HeartDisease : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart_disease)
    findViewById<WebView>(R.id.web).apply {
        settings.javaScriptEnabled=true
        loadUrl("http://3.135.227.34:5000/prediction")
        if(WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)){
            WebSettingsCompat.setAlgorithmicDarkeningAllowed(this.settings,true)
        }
    }



            onBackPressedDispatcher.addCallback(object :OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    finish()
                }
            })
    }
}