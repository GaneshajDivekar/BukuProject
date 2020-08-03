package com.example.demoproject.core.network

import com.example.demoproject.data.api.usermodel.UserListModel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET


public interface RetrofitApiInterface {


    @GET(WebServiceAPI.UserList)
    abstract fun userList(
    ): Deferred<Response<UserListModel>>


}


