package com.example.chronicledisease.presentation.commonpack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import com.example.chronicledisease.R
import com.example.chronicledisease.ResponsiveLayer.DataBase
import com.example.chronicledisease.databinding.ActivityLoginBinding
import com.example.chronicledisease.ml.Model
import com.example.chronicledisease.presentation.user.UserMainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private val bind by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val data by lazy {
        DataBase.getcontext(this).dao()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

bind.signinbtn.setOnClickListener {
    val mail=bind.mail.text.toString().trim()
    val password=bind.password.text.toString().trim()

    if(mail.isEmpty()){
        it.snackView("Please enter Mail-id")
    }else if(password.isEmpty()){
        it.snackView("Please enter Password")
    }else{
        CoroutineScope(IO).launch {
            data.checkuser(mail = mail, password = password)?.let {
                withContext(Main){
                    if(it.isEmpty()){
                        showToast("Invalid user")
                    }else{
                        getSharedPreferences("user", MODE_PRIVATE)
                            .edit().putString("type","user")
                            .putString("name",it[0].name)
                            .putString("mobile",it[0].mobile)
                            .putString("mail",it[0].mail)
                            .putString("password",it[0].password)
                            .apply()
                        finish()
                        startActivity(Intent(this@LoginActivity,UserMainActivity::class.java))
                    }
                }
            }
        }
    }

}
        bind.signup.setOnClickListener { startActivity(Intent(this,SignupActivity::class.java)) }
    }
}