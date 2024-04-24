package com.example.chronicledisease.ResponsiveLayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chronicledisease.ResponsiveLayer.Entities.Detectionstable
import com.example.chronicledisease.ResponsiveLayer.Entities.UserTable

@Database(entities = [UserTable::class, Detectionstable::class], version = 1, exportSchema = false)
abstract class DataBase :RoomDatabase(){
    abstract fun dao():Dao
    companion object{
        fun getcontext(context: Context):DataBase{
            return Room.databaseBuilder(context,DataBase::class.java,"DataBase").build()
        }
    }
}