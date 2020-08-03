package com.example.demoproject.core.presentation.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 *
 * Generic Base Activity used to inflat with its respective view data binding, activity model.
 *
 * */
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity() {

    private lateinit var mViewDataBinding: T



    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    abstract fun setUp(savedInstanceState: Bundle?)


    override fun attachBaseContext(newBase: Context) {
        //injectFeature() // Commented until lazy loading koin-modules in dynamic module migration.
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        setUp(savedInstanceState)
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding
    }


    fun getSupportFragManager(): FragmentManager {
        return supportFragmentManager
    }

    fun openFragment(frameId: Int, fragment: Fragment) {
        getSupportFragManager().beginTransaction()
            .replace(frameId, fragment, fragment::class.java.toString())
            .commit()
    }

    fun addFragment(frameId: Int, fragment: Fragment) {
        getSupportFragManager().beginTransaction()
            .add(frameId, fragment, fragment::class.java.toString())
            .commit()
    }



}


