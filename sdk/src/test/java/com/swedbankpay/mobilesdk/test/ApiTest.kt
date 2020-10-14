package com.swedbankpay.mobilesdk.test

import com.google.gson.JsonParser
import com.nhaarman.mockitokotlin2.mock
import com.swedbankpay.mobilesdk.Consumer
import com.swedbankpay.mobilesdk.merchantbackend.MerchantBackendConfiguration
import com.swedbankpay.mobilesdk.internal.remote.Api
import com.swedbankpay.mobilesdk.merchantbackend.UnexpectedResponseException
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness

/**
 * Tests for HTTP requests made to the merchant backend
 */
class ApiTest {
    companion object {
        private const val consumersPath = "/consumers"
        private const val paymentordersPath = "/paymentorders"
        private const val rootResponse = """{"consumers":"$consumersPath","paymentorders":"$paymentordersPath"}"""
    }

    /**
     * STRICT_STUBS mockito rule for cleaner tests
     */
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    private lateinit var server: MockWebServer

    private lateinit var configuration: MerchantBackendConfiguration

    private fun response(body: String) = MockResponse().apply {
        setResponseCode(200)
        addHeader("Content-Type", "application/json")
        setBody(body)
    }

    /**
     * Set up MockServer
     */
    @Before
    fun setup() {
        Api.skipProviderInstallerForTests()

        server = MockWebServer()
        server.start()

        configuration = MerchantBackendConfiguration.Builder(server.url("/").toString()).build()
    }

    /**
     * Shut down MockServer
     */
    @After
    fun teardown() {
        server.shutdown()
    }

    /**
     * Check that root link is used in a GET request
     */
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

    /**
     * Check that root link accepts a correctly formed json response
     */
    @Test
    fun rootLinkShouldAcceptValidResponse() {
        server.enqueue(response(rootResponse))
        val resources = runBlocking {
            configuration.rootLink.get(mock(), configuration)
        }.value
        Assert.assertEquals(server.url(consumersPath), resources.consumers.href)
        Assert.assertEquals(server.url(paymentordersPath), resources.paymentorders.href)
    }

    /**
     * Check that root link rejects an invalid response
     */
    @Test(expected = UnexpectedResponseException::class)
    fun rootLinkShouldRejectInvalidResponse() {
        server.enqueue(response("invalid"))
        runBlocking {
            configuration.rootLink.get(mock(), configuration)
        }
    }

    /**
     * Check that consumers link is used in a POST request with correctly formed json body
     */
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

    /**
     * Check that paymentorders link is used in a POST request with correctly formed json body
     */
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