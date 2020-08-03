package com.example.demoproject.ui.mainModule

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

class MainViewModel(application: Application) : BaseViewModel(application) {

}
