package com.kudashov.hangoverkitchenconversation.net

import com.kudashov.hangoverkitchenconversation_android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun OkHttpClient.Builder.addHttpLoggingInterceptor() = apply {
    if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        this.addNetworkInterceptor(loggingInterceptor)
    }
}