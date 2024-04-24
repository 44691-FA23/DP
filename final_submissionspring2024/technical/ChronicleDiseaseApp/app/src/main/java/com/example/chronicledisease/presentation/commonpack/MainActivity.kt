package com.example.chronicledisease.presentation.commonpack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chronicledisease.R
import com.example.chronicledisease.databinding.ActivityMainBinding
import com.example.chronicledisease.presentation.user.UserMainActivity

class MainActivity : AppCompatActivity() {
    private val bind by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        bind.heart.let {
            val type=getSharedPreferences("user", MODE_PRIVATE).getString("type","")
            it.alpha=0f
            it.animate().alpha(1f).setDuration(500).withEndAction {
                finish()
                if(type=="user"){
                    startActivity(Intent(this,UserMainActivity::class.java))
                }else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }


        }

    }
}