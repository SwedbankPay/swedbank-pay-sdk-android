package com.swedbankpay.mobilesdk.internal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import androidx.lifecycle.MutableLiveData

// The purpose of this is to provide a way for external applications
// to bounce back to our task. It will just immediately finish itself
// to reveal the previous top activity, which should be the one
// that started the external activity.
internal class CallbackActivity : Activity() {
    companion object {
        val onReloadPaymentMenu = MutableLiveData<Unit>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure we get back to the proper task.
        // What we want to do here is bring the task that started
        // the external activity to foreground. The easiest way to
        // do this is to start an activity with the same task affinity
        // with the Intent.FLAG_ACTIVITY_NEW_TASK flag.
        if (intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK == 0) {
            startActivity(Intent(this, CallbackActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } else {
            // See comment at PaymentViewModel.retryPreviousAction()
            onReloadPaymentMenu.value = Unit
            onReloadPaymentMenu.value = null
        }
        finish()
    }
}
