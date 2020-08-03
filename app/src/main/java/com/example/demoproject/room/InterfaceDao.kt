package com.example.demoproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.demoproject.data.db.UserListEntity


@Dao
interface InterfaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(customerListEntity: UserListEntity)

    @Query("SELECT * FROM user_info_list")
    fun getAllUserList(): List<UserListEntity>


    @Query("Delete from user_info_list")
    fun deleteAllUsers()


}





