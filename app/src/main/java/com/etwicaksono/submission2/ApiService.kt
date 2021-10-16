package com.etwicaksono.submission2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token<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>")

    @GET("users")
    fun getAllUsers():Call<List<ResponseUserItem>>

    @GET("search/user")
    fun searchUser(@Query("q")q:String):Call<ResponseUser>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") username:String):Call<ResponseUserItem>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String):Call<ResponseUser>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username")username: String):Call<ResponseUser>

}