package com.demo.movies.data.remote

import com.demo.movies.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl: HttpUrl = originalRequest.url()

        return originalUrl.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build().let { urlWithApiKey ->
                originalRequest.newBuilder()
                    .url(urlWithApiKey)
                    .build()
            }.let(chain::proceed)
    }
}