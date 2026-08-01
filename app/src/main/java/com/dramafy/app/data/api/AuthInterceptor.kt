package com.dramafy.app.data.api

import com.dramafy.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.AUTH_TOKEN}")
            .header("User-Agent", "Mozilla/5.0")
            .header("Accept", "application/json")
            .build()
        return chain.proceed(newRequest)
    }
}
