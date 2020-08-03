package com.example.demoproject.ui.fragment.homefmodule

import com.example.demoproject.data.db.UserListEntity

interface HomeNavigator {
    fun clickOnCustomerItemView(customerListEntity: UserListEntity)
}