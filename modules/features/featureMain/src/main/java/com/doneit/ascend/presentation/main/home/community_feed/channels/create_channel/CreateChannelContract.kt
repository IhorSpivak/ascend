package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.add_members.AddMembersContract
import com.doneit.ascend.presentation.models.PresentationCreateChannelModel

interface CreateChannelContract {
    interface ViewModel : BaseViewModel, AddMembersContract.ViewModel {
        val newChannelModel: PresentationCreateChannelModel
        override val connectionAvailable: MutableLiveData<Boolean>

        fun onPhotoSelected(sourceUri: Uri, destinationUri: Uri, fragmentToReceiveResult: Fragment)
        fun setEditMode(channel: ChatEntity)
        fun addMembers()
    }

    interface Router {
        fun onBackWithOpenChannel(channel: ChatEntity)
        fun onBack()
        fun navigateToAddChannelMembers()
        fun navigateToAvatarUCropActivity(
            sourceUri: Uri,
            destinationUri: Uri,
            fragmentToReceiveResult: Fragment
        )
    }
    interface LocalRouter {
        fun onBack()
        fun navigateToAddChannelMembers()
    }
}