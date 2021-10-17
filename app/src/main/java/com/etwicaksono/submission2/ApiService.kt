package com.etwicaksono.submission2

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers("Authorization: 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")

    @GET("users")
    fun getAllUsers():Call<List<ResponseUserItem>>

    @GET("search/user")
    fun searchUser(
        @Query("q")q:String):Call<List<ResponseUserItem>>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") username:String):Call<ResponseUserDetail>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String):Call<List<ResponseUserItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username")username: String):Call<List<ResponseUserItem>>

}