package com.example.chronicledisease.ResponsiveLayer.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class UserTable (
    var name:String,
    var mobile:String,
 @PrimaryKey(autoGenerate = false) var mail:String,
    var password:String
)