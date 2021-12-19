package com.kudashov.hangoverkitchenconversation.net

import com.apollographql.apollo.ApolloClient
import com.kudashov.hangoverkitchenconversation.interactor.SharedPrefInteractor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class NetworkService {

    companion object {
        private var mInstance: NetworkService? = null

        fun getInstance(): NetworkService? {
            if (mInstance == null) {
                mInstance = NetworkService()
            }
            return mInstance
        }
    }

    private val BASE_URL = "https://afternoon-brushlands-65833.herokuapp.com/graphql"

    fun getApolloClient(): ApolloClient {
        val okHttp = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .addHttpLoggingInterceptor()
            .build()

        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp)
            .build()
    }

    fun getApolloClientWithTokenInterceptor(token: String): ApolloClient {

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()

                val builder: Request.Builder = original
                    .newBuilder()
                    .method(original.method, original.body)

                builder.header("Authorization", "Bearer $token")
                return@Interceptor chain.proceed(builder.build())
            })
            .connectTimeout(20, TimeUnit.SECONDS)
            .addHttpLoggingInterceptor()
            .build()

        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(httpClient)
            .build()
    }
}