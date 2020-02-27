package com.doneit.ascend.domain.entity.ascension

import com.doneit.ascend.domain.entity.IdentifiableEntity
import java.util.*

open class AscensionEntity(
    id: Long,
    val date: Date
): IdentifiableEntity(id)