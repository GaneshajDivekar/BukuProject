package com.example.demoproject.ui.fragment.homefmodule

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoproject.R
import com.example.demoproject.core.presentation.base.BaseFragment
import com.example.demoproject.data.db.UserListEntity
import com.example.demoproject.databinding.FragmentHomeBinding
import com.example.demoproject.room.DatabaseHelper
import com.example.demoproject.ui.fragment.homefmodule.adapter.HomeAdapter
import com.example.demoproject.ui.profilemodule.ProfileActivity
import com.example.demoproject.utils.DialogUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), HomeNavigator {
    var fragmentHomeBinding: FragmentHomeBinding? = null
    val homeViewModel: HomeViewModel by viewModel()
    var homeAdapter: HomeAdapter? = null
    var homeNavigator: HomeNavigator? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getViewModel(): HomeViewModel {
        return homeViewModel
    }

    override fun setUp(view: View, savedInstanceState: Bundle?) {
        fragmentHomeBinding = getViewDataBinding()
        fragmentHomeBinding!!.homeCallBack = this
        homeNavigator = this

        initview()
    }

    private fun initview() {

        DatabaseHelper.getDatabase(activity!!).interfaceDao().deleteAllUsers()
        homeViewModel.getUserList(
        ).observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                for (i in it.data!!.indices) {
                    var userListEntity = UserListEntity()
                    userListEntity.id = it.data!!.get(i).id!!.toInt()
                    userListEntity.email = it.data!!.get(i).email.toString()
                    userListEntity.first_name = it.data!!.get(i).firstName.toString()
                    userListEntity.last_name = it.data!!.get(i).lastName.toString()
                    userListEntity.avatar = it.data!!.get(i).avatar.toString()
                    DatabaseHelper.getDatabase(activity!!).interfaceDao().addUsers(userListEntity)


                }
                DialogUtils.stopProgressDialog()
                var userList = DatabaseHelper.getDatabase(activity!!).interfaceDao().getAllUserList()
                homeAdapter = HomeAdapter(
                    activity!!,
                    userList as ArrayList<UserListEntity>,
                    homeNavigator as HomeFragment
                )
                fragmentHomeBinding?.recyclerUserList?.setHasFixedSize(true)
                val mLayoutManager: RecyclerView.LayoutManager =
                    LinearLayoutManager(activity!!)
                fragmentHomeBinding?.recyclerUserList?.layoutManager = mLayoutManager
                fragmentHomeBinding?.recyclerUserList?.itemAnimator = DefaultItemAnimator()


                fragmentHomeBinding?.recyclerUserList?.adapter = homeAdapter

            } else {
                DialogUtils.stopProgressDialog()

            }
        })


    }

    override fun clickOnCustomerItemView(customerListEntity: UserListEntity) {
        var intent = Intent(activity!!, ProfileActivity::class.java)
        intent.putExtra("userlistInfo", customerListEntity)
        startActivity(intent)

    }


}