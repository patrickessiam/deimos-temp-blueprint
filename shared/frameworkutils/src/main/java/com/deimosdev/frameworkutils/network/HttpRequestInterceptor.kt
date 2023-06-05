package com.deimosdev.frameworkutils.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder().url(originalRequest.url).build()
        Timber.d(originalRequest.toString())
        return chain.proceed(newRequest)
    }
}
