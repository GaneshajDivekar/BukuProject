package com.example.demoproject.ui.profilemodule

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.demoproject.R
import com.example.demoproject.core.presentation.base.BaseActivity
import com.example.demoproject.data.db.UserListEntity
import com.example.demoproject.databinding.ActivityProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileMainViewModel>(),
    ProfileMainNavigator {
    var activityProfileBinding: ActivityProfileBinding? = null
    val profileMainViewModel: ProfileMainViewModel by viewModel()
    var userlistInfo: UserListEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProfileBinding = getViewDataBinding()
        activityProfileBinding!!.profileCallBack = this

        userlistInfo = intent.getParcelableExtra("userlistInfo")
        Glide.with(this@ProfileActivity)
            .load(userlistInfo?.avatar)
            .placeholder(R.drawable.ic_dummy_user)
            .into(activityProfileBinding?.imgUserProfile!!)

        activityProfileBinding!!.txtFirstNameResult.setText(userlistInfo?.first_name)
        activityProfileBinding!!.txtLastNameResult.setText(userlistInfo?.last_name)
        activityProfileBinding!!.txtEmailResult.setText(userlistInfo?.email)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_profile
    }

    override fun getViewModel(): ProfileMainViewModel {
        return profileMainViewModel
    }

    override fun setUp(savedInstanceState: Bundle?) {
    }

    override fun clickOnBack() {
        onBackPressed()
    }
}