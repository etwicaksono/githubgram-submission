package com.etwicaksono.githubgram

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val builder = request
                        .newBuilder()
                        .header("Authorization", "Bearer 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")
                        .method(request.method, request.body)
                    val mutatedRequest = builder.build()
                    val response = chain.proceed(mutatedRequest)
                    response
                }
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}