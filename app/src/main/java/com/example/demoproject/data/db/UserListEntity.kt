package com.example.demoproject.data.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.demoproject.room.dbHelper.DBUtils


@Entity(tableName = DBUtils.USER_LIST)
class UserListEntity() :Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DBUtils.UserListConstant.KEY_user_unique_id)
    var user_unique_id: Int = 0

    @ColumnInfo(name = DBUtils.UserListConstant.KEY_id)
    var id: Int = 0

    @ColumnInfo(name = DBUtils.UserListConstant.KEY_first_name)
    var first_name: String = ""

    @ColumnInfo(name = DBUtils.UserListConstant.KEY_last_name)
    var last_name: String = ""

    @ColumnInfo(name = DBUtils.UserListConstant.KEY_email)
    var email: String = ""

    @ColumnInfo(name = DBUtils.UserListConstant.KEY_avatar)
    var avatar: String = ""

    constructor(parcel: Parcel) : this() {
        user_unique_id = parcel.readInt()
        id = parcel.readInt()
        first_name = parcel.readString()
        last_name = parcel.readString()
        email = parcel.readString()
        avatar = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(user_unique_id)
        parcel.writeInt(id)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(email)
        parcel.writeString(avatar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserListEntity> {
        override fun createFromParcel(parcel: Parcel): UserListEntity {
            return UserListEntity(parcel)
        }

        override fun newArray(size: Int): Array<UserListEntity?> {
            return arrayOfNulls(size)
        }
    }
}