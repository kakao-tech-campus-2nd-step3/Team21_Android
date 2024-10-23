package com.example.everymoment.data.model.network.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

object NetworkUtil {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun <T> sendData(
        url: String,
        jwtToken: String? = null,
        data: T,
        callback: (success: Boolean, code: Int?, message: String?, infoObject: JsonObject?) -> Unit
    ) {
        val jsonBody = gson.toJson(data)
        val requestBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .apply {
                if (!jwtToken.isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer $jwtToken")
                }
            }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, null, null, null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonResponse = responseBody.string()
                    val jsonObject = gson.fromJson(jsonResponse, JsonObject::class.java)

                    val code = jsonObject.get("code").asInt
                    val message = jsonObject.get("message").asString
                    val infoObject = jsonObject.getAsJsonObject("info")

                    callback(true, code, message, infoObject)
                } ?: callback(false, null, null, null)
            }
        })
    }

    fun <T> getData(
        url: String,
        jwtToken: String? = null,
        queryParams: Map<String, String> = emptyMap(),
        responseClass: Class<T>,
        callback: (success: Boolean, response: T?) -> Unit
    ) {
        val httpUrlBuilder = url.toHttpUrlOrNull()?.newBuilder()
        if (queryParams.isNotEmpty()) {
            queryParams.forEach { (key, value) ->
                httpUrlBuilder?.addQueryParameter(key, value)
            }
        }
        val httpUrl = httpUrlBuilder?.build()

        val request = Request.Builder()
            .url(httpUrl!!)
            .get()
            .apply {
                if (!jwtToken.isNullOrEmpty()) {
                    addHeader("Authorization", "Bearer $jwtToken")
                }
            }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        try {
                            val result = gson.fromJson(responseBody.string(), responseClass)
                            callback(true, result)
                        } catch (e: Exception) {
                            callback(false, null)
                        }
                    } ?: callback(false, null)
                } else {
                    callback(false, null)
                }
            }
        })
    }
}