package com.example.demoproject.ui.fragment.homefmodule

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.demoproject.core.network.RetrofitCallAPI
import com.example.demoproject.core.network.WebServiceAPI
import com.example.demoproject.core.presentation.base.BaseViewModel
import com.example.demoproject.data.api.usermodel.UserListModel
import com.example.demoproject.utils.DialogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

 public class HomeViewModel(application: Application) : BaseViewModel(application) {
     private val uiScope = CoroutineScope(Dispatchers.Main)

     fun getUserList(
     ): LiveData<UserListModel> {
         val loginModel = MutableLiveData<UserListModel>()
         uiScope.launch {

             val resultDef: Deferred<Response<UserListModel>> =
                 getUserListData()
             try {
                 val result: Response<UserListModel> = resultDef.await()
                 if (result.isSuccessful) {
                     val response = result.body()
                     response?.let {
                         if (response != null) {
                             if (response.data != null) {
                                 loginModel.value = response
                             } else {
                                 DialogUtils.stopProgressDialog()
                                 Toast.makeText(
                                     getApplication(),
                                     "Try again",
                                     Toast.LENGTH_SHORT
                                 ).show()
                             }

                         } else {
                             DialogUtils.stopProgressDialog()
                             Toast.makeText(
                                 getApplication(),
                                 "Something went wrong",
                                 Toast.LENGTH_SHORT
                             )
                                 .show()
                         }


                     }
                 } else {
                     DialogUtils.stopProgressDialog()
                     Toast.makeText(
                         getApplication(),
                         "Please Check Internet Connections.",
                         Toast.LENGTH_SHORT
                     )
                         .show()
                 }
             } catch (ex: Exception) {

                 DialogUtils.stopProgressDialog()
                 resultDef.getCompletionExceptionOrNull()?.let {
                     println(resultDef.getCompletionExceptionOrNull()!!.message)
                 }

             }
         }
         return loginModel
     }

     private fun getUserListData(
     ): Deferred<Response<UserListModel>> {
         return RetrofitCallAPI.getInstance(WebServiceAPI.SERVERBASE_URL)!!.userList(
         )
     }
}
