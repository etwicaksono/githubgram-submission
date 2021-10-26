package com.etwicaksono.githubgram.responses

import com.google.gson.annotations.SerializedName

data class ResponseUserItem(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,
)
