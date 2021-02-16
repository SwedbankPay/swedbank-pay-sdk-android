package com.swedbankpay.mobilesdk.internal

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.swedbankpay.mobilesdk.R

internal class ToSActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_URL = "EXTRA_URL"
    }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.swedbankpaysdk_tos_activity)
        swipeRefreshLayout = findViewById(R.id.swedbankpaysdk_swipe_refresh_layout)
        webView = findViewById(R.id.swedbankpaysdk_web_view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupWebView()
        loadContent()
    }

    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                swipeRefreshLayout.isRefreshing = false
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            loadContent(fromSwipe = true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    private fun loadContent(fromSwipe: Boolean = false) {
        if (!fromSwipe) swipeRefreshLayout.isRefreshing = true

        webView.apply {
            clearHistory()
            loadUrl(intent.getStringExtra(EXTRA_URL))
        }
    }
}