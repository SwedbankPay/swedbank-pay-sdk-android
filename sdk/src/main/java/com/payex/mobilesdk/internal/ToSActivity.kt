package com.payex.mobilesdk.internal

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.payex.mobilesdk.R
import kotlinx.android.synthetic.main.payexsdk_tos_fragment.*

internal class ToSActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_URL = "EXTRA_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payexsdk_tos_fragment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupWebView()
        loadContent()
    }

    private fun setupWebView() {
        payexsdk_web_view.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    payexsdk_swipe_refresh_layout.isRefreshing = false
                }
            }
        }

        payexsdk_swipe_refresh_layout.setOnRefreshListener {
            loadContent(fromSwipe = true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadContent(fromSwipe: Boolean = false) {
        if (!fromSwipe) payexsdk_swipe_refresh_layout.isRefreshing = true

        payexsdk_web_view.apply {
            clearHistory()
            payexsdk_web_view.loadUrl(intent.getStringExtra(EXTRA_URL))
        }
    }
}