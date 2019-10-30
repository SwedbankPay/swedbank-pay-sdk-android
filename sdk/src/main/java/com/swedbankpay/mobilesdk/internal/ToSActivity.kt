package com.swedbankpay.mobilesdk.internal

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.swedbankpay.mobilesdk.R
import kotlinx.android.synthetic.main.swedbankpaysdk_tos_activity.*

internal class ToSActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_URL = "EXTRA_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.swedbankpaysdk_tos_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupWebView()
        loadContent()
    }

    private fun setupWebView() {
        swedbankpaysdk_web_view.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    swedbankpaysdk_swipe_refresh_layout.isRefreshing = false
                }
            }
        }

        swedbankpaysdk_swipe_refresh_layout.setOnRefreshListener {
            loadContent(fromSwipe = true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadContent(fromSwipe: Boolean = false) {
        if (!fromSwipe) swedbankpaysdk_swipe_refresh_layout.isRefreshing = true

        swedbankpaysdk_web_view.apply {
            clearHistory()
            swedbankpaysdk_web_view.loadUrl(intent.getStringExtra(EXTRA_URL))
        }
    }
}