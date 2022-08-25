package com.etwicaksono.githubgram.api

import com.etwicaksono.githubgram.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val token = BuildConfig.GITHUB_KEY

            val loggingInterceptor =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                    val builder = request
                        .newBuilder()
                        .header("Authorization", token)
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