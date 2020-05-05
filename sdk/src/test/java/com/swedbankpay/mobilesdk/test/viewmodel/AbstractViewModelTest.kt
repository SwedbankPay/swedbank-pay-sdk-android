package com.swedbankpay.mobilesdk.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import org.junit.After

abstract class AbstractViewModelTest : ViewModelStoreOwner {
    private val viewModelStore = ViewModelStore()
    override fun getViewModelStore() = viewModelStore

    inline fun <reified T : ViewModel> getViewModel() = ViewModelProvider(this)[T::class.java]

    @After
    fun clearViewModelStore() {
        viewModelStore.clear()
    }
}