package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class CardsResponse(
    @SerializedName("cards") val cards: List<CardResponse>,
    @SerializedName("count") val count: Int
)