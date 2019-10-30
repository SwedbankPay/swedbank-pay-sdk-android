package com.swedbankpay.mobilesdk.test.json

import com.google.gson.GsonBuilder
import com.swedbankpay.mobilesdk.internal.remote.json.Link
import okhttp3.HttpUrl
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass

class LinkTest {

    private fun parseLink(type: KClass<out Link>) {
        val baseUrl = HttpUrl.get("https://test.invalid/")
        val path = "/path"
        val resolvedUrl = HttpUrl.get("https://test.invalid/path")

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

    @Test
    fun parseLinks() {
        Link::class.sealedSubclasses.forEach(::parseLink)
    }
}