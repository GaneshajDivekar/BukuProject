package com.example.demoproject.ui.mainModule

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.demoproject.R
import com.example.demoproject.core.presentation.base.BaseActivity
import com.example.demoproject.data.db.UserListEntity
import com.example.demoproject.databinding.ActivityMainBinding
import com.example.demoproject.room.DatabaseHelper
import com.example.demoproject.ui.fragment.homefmodule.HomeFragment
import com.example.demoproject.ui.fragment.profilemodule.ProfileFragment
import com.example.demoproject.utils.DialogUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator,
    BottomNavigationView.OnNavigationItemSelectedListener {

    var activityMainBinding: ActivityMainBinding? = null
    val mainViewModel: MainViewModel by viewModel()


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        return mainViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = getViewDataBinding()
        activityMainBinding?.mainviewCallback = this
        DialogUtils.startProgressDialog(this@MainActivity);
        initView(this@MainActivity)

    }

    private fun initView(mainActivity: MainActivity) {

        loadFragment(HomeFragment())
        val navigation: BottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mainActivity)


    }

    override fun setUp(savedInstanceState: Bundle?) {

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.navigation_home -> fragment = HomeFragment()
            R.id.navigation_profile -> fragment = ProfileFragment()

        }

        return loadFragment(fragment)
    }
}
