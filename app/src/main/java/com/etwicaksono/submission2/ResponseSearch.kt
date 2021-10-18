package com.etwicaksono.submission2

import com.google.gson.annotations.SerializedName

data class ResponseSearch(

    @field:SerializedName("total_count")
    val totalCount: Int? = null,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    @field:SerializedName("items")
    val items: List<ResponseUserItem>? = null
)

