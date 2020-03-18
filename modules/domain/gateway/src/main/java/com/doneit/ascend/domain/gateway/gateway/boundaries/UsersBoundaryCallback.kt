package com.doneit.ascend.domain.gateway.gateway.boundaries

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity

class UsersBoundaryCallback : PagedList.BoundaryCallback<AttendeeEntity>() {
    override fun onItemAtEndLoaded(itemAtEnd: AttendeeEntity) {
        super.onItemAtEndLoaded(itemAtEnd)
    }

    override fun onItemAtFrontLoaded(itemAtFront: AttendeeEntity) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}