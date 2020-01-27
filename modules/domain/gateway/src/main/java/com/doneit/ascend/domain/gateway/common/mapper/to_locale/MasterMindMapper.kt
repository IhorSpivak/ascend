package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.ThumbnailEntity
import com.doneit.ascend.domain.entity.dto.MasterMindListModel
import com.doneit.ascend.source.storage.local.data.ImageLocal
import com.doneit.ascend.source.storage.local.data.MasterMindLocal
import com.doneit.ascend.source.storage.local.data.ThumbnailLocal
import com.doneit.ascend.source.storage.local.data.dto.MMFilter

fun MasterMindEntity.toLocal(): MasterMindLocal {
    return MasterMindLocal(
        id,
        fullName,
        displayName,
        description,
        location,
        bio,
        groupsCount,
        rating,
        followed,
        rated,
        image?.toLocal(),
        allowRating,
        myRating
    )
}

fun ImageEntity.toLocal(): ImageLocal {
    return ImageLocal(
        -1,
        url,
        thumbnail?.toLocal()
    )
}

fun ThumbnailEntity.toLocal(): ThumbnailLocal {
    return ThumbnailLocal(
        -1,
        url
    )
}

fun MasterMindListModel.toLocal(): MMFilter {
    return MMFilter(
        fullName,
        displayName,
        followed,
        rated
    )
}