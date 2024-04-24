package com.example.chronicledisease.presentation.user

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.chronicledisease.R
import com.example.chronicledisease.dataViewLayer.Decider
import com.example.chronicledisease.dataViewLayer.viewmodel.StateView
import com.example.chronicledisease.databinding.ActivityCommonactBinding
import com.example.chronicledisease.presentation.commonpack.spanned

class CommonAct : AppCompatActivity() {
    private val bind by lazy {
        ActivityCommonactBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[StateView::class.java]
    }
private val decide by lazy {
    Decider(this)
}
    override fun onCreate(view: Bundle?) {
        super.onCreate(view)
        setContentView(bind.root)
        viewModel.mutable().observe(this) {
            if (it != null) {
                 bind.imagesetter.load(it)

                intent.getStringExtra("type")?.apply {
                    if(contains("Lung")){
                        decide.commonActivity(bitmap =it,bind.results)
                    }else if(contains("Alzheimer")){
                        decide.lungdisease(bitmap =it, result =bind.results)
                    }else if(contains("Skin")){
                    decide.skindisease(bitmap =it, result =bind.results)
                    }
                }

            }
        }

        intent.getStringExtra("type")?.let {
            bind.type.text= spanned(message = it)
            val int=if(it.contains("Lung")){
                R.drawable.lungs
            }else if(it.contains("Alzheimer")){
                 R.drawable.brainval
            } else if(it.contains("Skin")){
                    R.drawable.skin
            }else {
                0
            }
            bind.imagesetter.load(int)
        }





        bind.gallery.setOnClickListener {
            local.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            })
        }
   /*     bind.camera.setOnClickListener {
            camera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }*/

    }



/*    private val camera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.extras?.getParcelable("data", Bitmap::class.java)
                } else {
                    it.data?.extras?.get("data") as Bitmap
                })?.let { it1 ->
                    viewmodel.getbitmap(it1)
                }
            }
        }*/

    private val local = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            it?.data?.data?.let { the ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val k=ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, the))
                    k.copy(Bitmap.Config.ARGB_8888,true)
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, the)
                }?.let {
                    viewModel.getbitmap(it)
                }
            }
        }
}