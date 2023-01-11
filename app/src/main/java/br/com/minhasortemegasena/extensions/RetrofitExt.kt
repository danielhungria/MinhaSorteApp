package br.com.minhasortemegasena.extensions

import android.util.Log
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer

fun Request.getHeaders() = if (headers().size() == 0) "No headers" else headers().toString()

fun RequestBody.getJsonString(): String = try {
    val buffer = Buffer()
    writeTo(buffer)
    buffer.readUtf8().toString()
} catch (e: Exception) {
    Log.e("GetJsonStringError", "Can't get Json String from Response Body")
    "Can't get Json String from Response Body"
}