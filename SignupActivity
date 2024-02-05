package com.example.chronicledisease.presentation.commonpack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chronicledisease.ResponsiveLayer.DataBase
import com.example.chronicledisease.ResponsiveLayer.Entities.Detectionstable
import com.example.chronicledisease.ResponsiveLayer.Entities.UserTable
import com.example.chronicledisease.databinding.ActivitySignupBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {
    private val bind by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val data by lazy {
        DataBase.getcontext(this).dao()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
    bind.signin.setOnClickListener {
        finish()
    }

        bind.signupbtn.setOnClickListener {view->
            val name=bind.name.text.toString().trim()
            val mobile=bind.mobile.text.toString().trim()
            val mailid=bind.mailid.text.toString().trim()
            val pass=bind.pass.text.toString().trim()
            if(name.isEmpty()){
                typevery(message = "Name", view =view)
            }else if(mobile.isEmpty()){
                typevery(message = "Mobile", view =view)
            }else if(mailid.isEmpty()){
                typevery(message = "Mail-id", view =view)
            }else if(pass.isEmpty()){
            typevery(message = "Password", view =view)
            }else if(!mailid.contains("@gmail.com")){
            view.snackView(message = "Please enter a valid Mail-id")
            }else if(mobile.length!=10){
                view.snackView(message ="Please enter a valid mobile number")
            }else{
                CoroutineScope(IO).launch {
                    data.createuser(
                        userTable = UserTable(
                            name = name,
                            mobile = mobile,
                            mail = mailid,
                            password = pass
                        )
                    )?.let {
                        withContext(Main){
                            if(it>0){
                                view.snackView("Created")
                                data.createStatics(
                                    detectionstable = Detectionstable(
                                        usermail = mailid,
                                        alzheimer = 0,
                                         lungdisease =  0,
                                        skindisease = 0
                                    )
                                )
                                finish()
                            }else{
                                view.snackView("Try with another email")
                            }
                        }
                    }
                }
            }
        }
    }
    private fun typevery(message:String,view: View){
        view.snackView("Please enter your $message")
    }



}