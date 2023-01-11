package br.com.minhasortemegasena.retrofit

import android.util.Log
import br.com.minhasortemegasena.extensions.getHeaders
import br.com.minhasortemegasena.extensions.getJsonString
import okhttp3.Interceptor
import okhttp3.Response

class RemoteLogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request).apply {
            Log.d(
                "LOG INTERCEPTOR",
                """
                    request: ${request.url()} ${request.method()}
                    headers: ${request.getHeaders()}
                    request body: ${request.body()?.getJsonString()}
                    HTTP Status Code ${code()}
                    response body: ${peekBody(Long.MAX_VALUE).string()}
                """.trimIndent()
            )
        }
    }
}