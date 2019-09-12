package com.payex.mobilesdk.test.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.payex.mobilesdk.internal.remote.json.Link
import okhttp3.*
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