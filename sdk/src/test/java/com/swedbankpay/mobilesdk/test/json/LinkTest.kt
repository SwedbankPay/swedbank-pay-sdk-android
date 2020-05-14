package com.swedbankpay.mobilesdk.test.json

import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.internal.remote.json.Link
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass

/**
 * Tests for Links
 */
class LinkTest {

    private fun parseLink(type: KClass<out Link>) {
        val baseUrl = "https://test.invalid/".toHttpUrl()
        val path = "/path"
        val resolvedUrl = "https://test.invalid/path".toHttpUrl()

        val request = Request.Builder()
            .url(baseUrl)
            .build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .build()
        val gson = GsonBuilder()
            .registerTypeHierarchyAdapter(Link::class.java, Link.getDeserializer(response))
            .create()

        val json = "\"$path\""
        val link = gson.fromJson(json, type.java)
        Assert.assertTrue("parse link class is $type", type.isInstance(link))
        Assert.assertEquals("link href is $resolvedUrl", link.href, resolvedUrl)
    }

    /**
     * Check that all Link subclasses are deserialized correctly
     */
    @Test
    fun parseLinks() {
        Link::class.sealedSubclasses.forEach(::parseLink)
    }
}