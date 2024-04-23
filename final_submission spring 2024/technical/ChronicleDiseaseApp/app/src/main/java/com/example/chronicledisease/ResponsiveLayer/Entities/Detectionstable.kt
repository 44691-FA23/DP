package com.example.chronicledisease.ResponsiveLayer.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Detectionstable")
data class Detectionstable(
    var usermail:String,
    var alzheimer:Int,
    var lungdisease:Int,
    var skindisease:Int
){
    @PrimaryKey(autoGenerate = true)var num=0
}