package com.payex.payexexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.payex.mobilesdk.*
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private var isSetup = false
        private fun setupApp() {
            if (!isSetup) {
                isSetup = true
                Timber.plant(Timber.DebugTree())
                PaymentFragment.defaultConfiguration =
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
        }
    }

    init {
        setupApp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            addPaymentFragment()
        }

        ViewModelProviders.of(this)[PaymentViewModel::class.java].initializationErrorDescription.observe(this, androidx.lifecycle.Observer {
            it?.code?.let { Timber.d("Uh-oh, got a $it response from backend.") }
        })
    }

    private fun addPaymentFragment() {
        val data = mapOf(
            "basketId" to UUID.randomUUID().toString(),
            "currency" to "SEK",
            "languageCode" to "sv-SE",
            "items" to arrayOf(
                mapOf(
                    "itemId" to "1",
                    "quantity" to 1,
                    "price" to 1250,
                    "vat" to 250
                )
            )
        )

        val fragment = PaymentFragment().apply {
            setArguments(PaymentFragment.ArgumentsBuilder()
                //.consumer(Consumer.Identified("NO"))
                .merchantData(data)
            )
        }
        supportFragmentManager.beginTransaction()
            .add(R.id.payment, fragment)
            .commit()
    }
}
