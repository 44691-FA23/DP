package com.example.chronicledisease.presentation.user

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.example.chronicledisease.R
import com.example.chronicledisease.ResponsiveLayer.DataBase
import com.example.chronicledisease.ResponsiveLayer.Entities.UserTable
import com.example.chronicledisease.databinding.ActivityProfileBinding
import com.example.chronicledisease.databinding.CardviewmodeBinding
import com.example.chronicledisease.presentation.commonpack.LoginActivity
import com.example.chronicledisease.presentation.commonpack.showToast
import com.example.chronicledisease.presentation.commonpack.spanned
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Profile : AppCompatActivity() {
    private val bind by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private val shared by lazy {
        getSharedPreferences("user", MODE_PRIVATE)
    }
    private val data by lazy {
        DataBase.getcontext(this).dao()
    }
    private val dialogbind by lazy {
        CardviewmodeBinding.inflate(layoutInflater)
    }
    private val dialog by lazy {
        Dialog(this).apply {
            setContentView(dialogbind.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        stateupdate()

        CoroutineScope(IO).launch {
            data.getdata(mail = "${shared.getString("mail", "")}")?.let {
                withContext(Main) {
                    if (it.isNotEmpty()) {
                        bind.alzheimers.text = spanned("${it[0].alzheimer}")
                        bind.lungscount.text = spanned("${it[0].lungdisease}")
                        bind.skincare.text = spanned("${it[0].skindisease}")
                    }
                }
            }
        }
        with(dialogbind){
            nameva.setText(spanned("${shared.getString("name","")}"))
            password.setText(spanned("${shared.getString("password","")}"))
        }

        bind.update.setOnClickListener {
            dialog.show()
        }
        bind.logout.setOnClickListener {
            myview()
        }

        dialogbind.update.setOnClickListener {
            val name=dialogbind.nameva.text.toString().trim()
            val password=dialogbind.password.text.toString().trim()
        if(name.isEmpty()){
            showToast("Please enter your Name")
        }else if(password.isEmpty()){
            showToast("Please enter your Mobile")
        }else{

             CoroutineScope(IO).launch {
                 data.updateUser(userTable = UserTable(name = name, mobile = "${shared.getString("mobile","")}", mail = "${shared.getString("mail","")}"
                     , password = password))
            withContext(Main){
                shared.edit().apply {
                    putString("name",name)
                    putString("password",password)
                apply()
                }
                finish()
            }

             }


        }

        }
        dialogbind.cancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun myview() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Do you want to Logout ??")
            setPositiveButton("Yes"){l,_->
                l.dismiss()
                finishAffinity()
                startActivity(Intent(this@Profile,LoginActivity::class.java))
            }
            setNegativeButton("No"){p,_->
                p.dismiss()
            }
            show()
        }
    }

    private fun stateupdate() {
        shared?.let {
            var t = ""
            it.getString("name", "")?.forEachIndexed { index, c ->
                t += if (index == 0) {
                    c.uppercaseChar()
                } else {
                    c
                }
            }
            bind.chronicle.text = spanned(t)
        }
    }

}