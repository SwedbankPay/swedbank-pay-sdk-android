package com.swedbankpay.exampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.swedbankpay.mobilesdk.*
import com.swedbankpay.exampleapp.products.productsViewModel
import kotlinx.android.synthetic.main.activity_main.*

private const val ALERT_TAG = "com.swedbankpay.exampleapp.alert"

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        private var isSetup = false
        private fun setupApp() {
            if (!isSetup) {
                isSetup = true
                PaymentFragment.defaultConfiguration = buildPaymentConfiguration()
            }
        }
        private fun buildPaymentConfiguration() =
            Configuration.Builder("https://payex-merchant-samples.appspot.com/")
                .requestDecorator(object : RequestDecorator() {
                    override fun decorateAnyRequest(userHeaders: UserHeaders, method: String, url: String, body: String?) {
                        userHeaders
                            .add("x-payex-sample-apikey", "c339f53d-8a36-4ea9-9695-75048e592cc0")
                            .add("x-payex-sample-access-token", "token123")
                    }
                })
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupApp()

        super.onCreate(savedInstanceState)
        savedInstanceState?.let(mainViewModel::resumeFromSavedState)
        observePaymentProcess()
        observeErrorMessage()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainViewModel.saveState(outState)
    }

    private fun observePaymentProcess() {
        // N.B! The PaymentViewModel is observed at the Activity level here,
        // because the ProductsFragment will be in a stopped state when the
        // PaymentFragment is visible, and as such it would not receive any callbacks.
        // If PaymentFragment would instead be shown as a child fragment
        // of ProductsFragment, then we could have this observation in ProductsFragment.
        paymentViewModel.richState.observe(this, Observer {
            if (it.state == PaymentViewModel.State.FAILURE) {
                mainViewModel.setErrorMessageFromState(it)
            }
            if (it.state.isFinal) {
                (nav_host as NavHostFragment)
                    .navController
                    .apply {
                        popBackStack(R.id.productsFragment, false)
                        if (it.state == PaymentViewModel.State.SUCCESS) {
                            productsViewModel.clearCart()
                            navigate(R.id.action_productsFragment_to_successFragment)
                        }
                    }
            }
        })
    }

    private fun observeErrorMessage() {
        mainViewModel.currentErrorMessage.observe(this, Observer {
            if (it != null) {
                supportFragmentManager.apply {
                    if (findFragmentByTag(ALERT_TAG) == null) {
                        PaymentErrorDialogFragment().show(this, ALERT_TAG)
                    }
                }
            }
        })
    }
}
