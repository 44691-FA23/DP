package com.example.chronicledisease.dataViewLayer.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StateView:ViewModel(){
    private val mutable=MutableLiveData<Bitmap?>()
    fun getbitmap(bitmap: Bitmap){
        mutable.postValue(bitmap)
    }
    fun mutable()=mutable
}