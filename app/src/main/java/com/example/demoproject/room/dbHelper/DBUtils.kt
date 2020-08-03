package com.example.demoproject.room.dbHelper

class DBUtils {
    companion object {
        const val DATABASE_NAME = "userInfo.db"
        const val DB_VERSION = 1
        const val USER_LIST = "user_info_list"


    }

    interface UserListConstant {
        companion object {
            const val KEY_user_unique_id = "user_unique_id"
            const val KEY_id = "id"
            const val KEY_email = "email"
            const val KEY_first_name = "first_name"
            const val KEY_last_name = "last_name"
            const val KEY_avatar= "avatar"
        }
    }


}
