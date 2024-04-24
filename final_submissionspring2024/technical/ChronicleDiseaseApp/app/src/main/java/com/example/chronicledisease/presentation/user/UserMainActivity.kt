package com.example.chronicledisease.presentation.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chronicledisease.dataViewLayer.StateDecider
import com.example.chronicledisease.dataViewLayer.UserView
import com.example.chronicledisease.R
import com.example.chronicledisease.databinding.ActivityUserMainBinding

class UserMainActivity : AppCompatActivity(), StateDecider {
    private val bind by lazy {
        ActivityUserMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        bind.cardView.setOnClickListener {
            startActivity(Intent(this, HeartDisease::class.java))
        }
     stateView()
        bind.imageView3.setOnClickListener{
startActivity(Intent(this,Profile::class.java))
        }

        bind.cycle.let {
            it.layoutManager = GridLayoutManager(this, 2)
            it.adapter = UserView(
                context = this,
                array = arrayListOf(R.drawable.brain,
                    R.drawable.lungs,
                    R.drawable.skin),
                name = arrayListOf("Alzheimer\ndisease", "Chronicle\ndisease","Skin\ndisease"),
                stateDecider = this,
                color =
                arrayListOf("#49e896", "#1e81b0","#8bd3fc")
            )
            it.isNestedScrollingEnabled = true
        }

    }

    @SuppressLint("SetTextI18n")
    private fun stateView() {
        var string=""
        getSharedPreferences("user", MODE_PRIVATE).getString("name","")
            ?.forEachIndexed { index, c ->
                if(index==0){
                    string+=c.uppercaseChar()
                }else{
                        if(index<=20){
                            string+=c.lowercase()
                        if(index==20){
                            string+="..."
                        }
                        }
                }
            }
        bind.textView6.text="Hi $string 😊!!"

    }

    override fun selectpositions(position: Int) {
        val inte = Intent(this, CommonAct::class.java)
        when (position) {
            0 -> {
                inte.putExtra("type", "<big>Alzheimer</big><br>disease")
            }
            1 -> {
                inte.putExtra(
                    "type", "<big>Lung</big><br>" +
                            "disease"
                )

            }
            2->{
                inte.putExtra("type","<big>Skin</big><br>disease")
            }

        }
        startActivity(inte)
    }

    override fun onResume() {
        super.onResume()
    stateView()
    }
}