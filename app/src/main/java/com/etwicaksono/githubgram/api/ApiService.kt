package com.etwicaksono.githubgram.api

import com.etwicaksono.githubgram.responses.ResponseUserDetail
import com.etwicaksono.githubgram.responses.ResponseUserItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getAllUsers(): Call<List<ResponseUserItem>>

    @GET("search/users")
    fun searchUser(
        @Query("q") q: String
    ): Call<List<ResponseUserItem>>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<ResponseUserDetail>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String): Call<List<ResponseUserItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") username: String): Call<List<ResponseUserItem>>

}