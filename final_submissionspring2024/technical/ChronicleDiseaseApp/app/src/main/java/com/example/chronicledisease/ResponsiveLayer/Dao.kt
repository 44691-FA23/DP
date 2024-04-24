package com.example.chronicledisease.ResponsiveLayer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.chronicledisease.ResponsiveLayer.Entities.Detectionstable
import com.example.chronicledisease.ResponsiveLayer.Entities.UserTable

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createuser(userTable: UserTable):Long?

    @Query("select * from usertable where mail=:mail and password=:password")
    suspend fun checkuser(mail:String,password:String):List<UserTable>?


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createStatics(detectionstable: Detectionstable)

    @Query("select * from detectionstable where usermail=:mail")
    suspend fun getdata(mail: String):List<Detectionstable>?

    @Update
    suspend fun updatetable(detectionstable: Detectionstable)
    @Update
    suspend fun updateUser(userTable: UserTable)
}