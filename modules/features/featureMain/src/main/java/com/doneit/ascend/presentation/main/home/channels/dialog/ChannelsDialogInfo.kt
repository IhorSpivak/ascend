package com.doneit.ascend.presentation.main.home.channels.dialog

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.dialog_bottom_sheet_channels.*

class ChannelsDialogInfo: BottomSheetDialogFragment() {

    private val args by lazy { arguments?.getParcelable<Args>(KEY_ARGS) }
    private var onJoinChannel: (ChatEntity) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.dialog_bottom_sheet_channels, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args?.channel?.let { channel ->
            titleChannel.text = channel.title
            when (channel.isPrivate) {
                true -> channelType.text = resources.getString(R.string.private_channel)
                false -> channelType.text = resources.getString(R.string.public_channel)
            }
            user_name.text = channel.owner?.fullName
            descriptionChannel.text = channel.description
            qtyMembers.text = "${channel.membersCount} members"
            Glide.with(this)
                .load(channel.image?.url)
                .apply(RequestOptions.circleCropTransform())
                .into(channelImage)

            Glide.with(this)
                .load(channel.owner?.image?.url)
                .apply(RequestOptions.circleCropTransform())
                .into(userIcon)

            btn_join.setOnClickListener {
                dismiss()
                onJoinChannel(channel)
            }
        }
    }

    companion object {

        private const val KEY_ARGS = "KEY_ARGS"

        @Parcelize
        private data class Args(
            val channel: ChatEntity
        ): Parcelable

        fun getInstance(channel: ChatEntity, onJoinChannel: (ChatEntity) -> Unit) = ChannelsDialogInfo().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_ARGS, Args(channel))
            }
            this.onJoinChannel = onJoinChannel
        }

    }

}