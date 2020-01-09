package com.swedbankpay.mobilesdk.internal

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.MutableLiveData

internal class CallbackActivity : Activity() {
    companion object {
        val onCallbackUrlInvoked = MutableLiveData<Unit>()
        private val invokedCallbackUrls = HashSet<CallbackUrl>()

        private fun onCallbackUrl(callbackUrl: CallbackUrl) {
            invokedCallbackUrls.add(callbackUrl)
            onCallbackUrlInvoked.value = Unit
        }
        fun consumeCallbackUrl(callbackUrl: CallbackUrl): Boolean {
            return invokedCallbackUrls.remove(callbackUrl)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure we get back to the proper task.
        // What we want to do here is bring the task that started
        // the external activity to foreground. The easiest way to
        // do this is to start an activity with the same task affinity
        // with the Intent.FLAG_ACTIVITY_NEW_TASK flag.
        val intent = intent
        if (intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK == 0) {
            startActivity(Intent(intent).also {
                it.setClass(this, CallbackActivity::class.java)
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } else {
            intent.data?.let {
                CallbackUrl.parse(this, it)
            }?.let(::onCallbackUrl)
        }
        finish()
    }
}
