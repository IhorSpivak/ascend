package com.doneit.ascend.presentation.models

import com.doneit.ascend.domain.entity.community_feed.Attachment

class PresentationCreatePostModel {
    var description: ValidatableField = ValidatableField()
    val media = arrayListOf<Attachment>()
    val deletedItemsId = arrayListOf<Long>()
}