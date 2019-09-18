package com.payex.mobilesdk.internal

import android.app.Activity
import android.os.Bundle

// The purpose of this is to provide a way for Swish inter-app
// to bounce back to our task. It will just immediately finish itself
// to reveal the previous top activity, which should be the one
// that started the Swish activity.
internal class CallbackActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}
