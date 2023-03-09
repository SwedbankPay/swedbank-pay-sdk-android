package com.swedbankpay.mobilesdk.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import org.junit.After

/**
 * Case class for ViewModel tests.
 *
 * This class implements ViewModelStoreOwner and acts as
 * a scope for ViewModels. The ViewModelStore is cleared after each test.
 */
abstract class AbstractViewModelTest : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
    
    /**
     * Convenience function: gets the ViewModel of class T backed by this object's ViewModelStore
     */
    inline fun <reified T : ViewModel> getViewModel() = ViewModelProvider(this)[T::class.java]

    /**
     * Clears the ViewModelStore
     */
    @After
    fun clearViewModelStore() {
        viewModelStore.clear()
    }
}