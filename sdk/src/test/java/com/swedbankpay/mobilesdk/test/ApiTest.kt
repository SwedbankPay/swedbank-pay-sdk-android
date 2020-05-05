package com.swedbankpay.mobilesdk.test

import com.google.gson.JsonParser
import com.nhaarman.mockitokotlin2.mock
import com.swedbankpay.mobilesdk.Configuration
import com.swedbankpay.mobilesdk.Consumer
import com.swedbankpay.mobilesdk.internal.remote.Api
import com.swedbankpay.mobilesdk.internal.remote.RequestProblemException
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ApiTest {
    companion object {
        private const val consumersPath = "/consumers"
        private const val paymentordersPath = "/paymentorders"
        private const val rootResponse = """{"consumers":"$consumersPath","paymentorders":"$paymentordersPath"}"""
    }

    private lateinit var server: MockWebServer

    private lateinit var configuration: Configuration

    private fun response(body: String) = MockResponse().apply {
        setResponseCode(200)
        addHeader("Content-Type", "application/json")
        setBody(body)
    }

    @Before
    fun setup() {
        Api.skipProviderInstallerForTests()

        server = MockWebServer()
        server.start()

        configuration = Configuration.Builder(server.url("/").toString()).build()
    }

    @After
    fun teardown() {
        server.shutdown()
    }

    @Test
    fun rootLinkShouldIssueGetRequest() {
        server.enqueue(response("{}"))
        runBlocking {
            try {
                configuration.rootLink.get(mock(), configuration)
            } catch (_: Exception) { }
        }
        val request = server.takeRequest()
        Assert.assertEquals("GET", request.method)
    }

    @Test
    fun rootLinkShouldAcceptValidResponse() {
        server.enqueue(response(rootResponse))
        val resources = runBlocking {
            configuration.rootLink.get(mock(), configuration)
        }.value
        Assert.assertEquals(server.url(consumersPath), resources.consumers.href)
        Assert.assertEquals(server.url(paymentordersPath), resources.paymentorders.href)
    }

    @Test(expected = RequestProblemException::class)
    fun rootLinkShouldRejectInvalidResponse() {
        server.enqueue(response("invalid"))
        runBlocking {
            configuration.rootLink.get(mock(), configuration)
        }
    }

    @Test
    fun consumersLinkShouldIssuePostRequest() {
        server.enqueue(response(rootResponse))
        val resources = runBlocking {
            configuration.rootLink.get(mock(), configuration)
        }.value
        server.takeRequest() // discard the request

        val consumer = Consumer(shippingAddressRestrictedToCountryCodes = emptyList())
        server.enqueue(response("{}"))
        runBlocking {
            resources.consumers.post(mock(), configuration, consumer)
        }

        val request = server.takeRequest()
        Assert.assertEquals("POST", request.method)
        val body = request.body.inputStream().reader().use(JsonParser::parseReader)
        Assert.assertTrue(body.isJsonObject)
        Assert.assertTrue(body.asJsonObject.get("shippingAddressRestrictedToCountryCodes")?.isJsonArray == true)
    }

    @Test
    fun paymentordersLinkShouldIssuePostRequest() {
        server.enqueue(response(rootResponse))
        val resources = runBlocking {
            configuration.rootLink.get(mock(), configuration)
        }.value
        server.takeRequest() // discard the request

        server.enqueue(response("{}"))
        runBlocking {
            resources.paymentorders.post(mock(), configuration, TestConstants.paymentOrder)
        }

        val request = server.takeRequest()
        Assert.assertEquals("POST", request.method)
        val body = request.body.inputStream().reader().use(JsonParser::parseReader)
        Assert.assertTrue(body.isJsonObject)
        Assert.assertTrue(body.asJsonObject.get("paymentorder")?.isJsonObject == true)
    }
}