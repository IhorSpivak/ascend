package com.doneit.ascend.presentation.main.chats.chat.common

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.chats.common.MyChatViewHolder
import com.doneit.ascend.presentation.main.databinding.ListItemMessageBinding
import com.doneit.ascend.presentation.utils.extensions.HOUR_12_ONLY_FORMAT
import com.doneit.ascend.presentation.utils.extensions.HOUR_24_ONLY_FORMAT
import com.doneit.ascend.presentation.utils.extensions.START_TIME_FORMATTER
import com.doneit.ascend.presentation.utils.extensions.calculateDate
import java.util.*
import kotlin.math.abs

class MessageViewHolder(
    private val binding: ListItemMessageBinding
): RecyclerView.ViewHolder(binding.root) {

    private var lastX: Float = 0F
    private var lastY: Float = 0F

    fun bind(messageEntity: MessageEntity, member: MemberEntity, user: UserEntity, nextMessage: MessageEntity?){
        binding.apply {
            this.memberEntity = member
            this.messageEntity = messageEntity
            this.user = user
            if(DateFormat.is24HourFormat(root.context)){
                this.sendTime = HOUR_24_ONLY_FORMAT.format(messageEntity.createdAt!!)
            }else{
                this.sendTime = HOUR_12_ONLY_FORMAT.format(messageEntity.createdAt!!)
            }
            if (nextMessage == null){
                time.text = START_TIME_FORMATTER.format(messageEntity.createdAt!!)
            }else {
                time.apply {
                    text = START_TIME_FORMATTER.format(messageEntity.createdAt!!)
                    corner.apply {
                        if(messageEntity.userId == nextMessage.userId){
                            binding.userImage.gone()
                            binding.isOnline.gone()
                            this.gone()
                        }else {
                            this.visible()
                        }
                    }
                    if (calculateDate(messageEntity.createdAt!!, nextMessage.createdAt!!)) {
                        this.visible()
                    } else {
                        this.gone()
                    }
                }
            }

        }
        itemView.setOnTouchListener { _, motionEvent ->
            var status = false

            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                lastX = motionEvent.rawX
                lastY = motionEvent.rawY
            }

            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (abs(lastX - motionEvent.rawX) < MOVEMENT_DELTA
                    && abs(lastY - motionEvent.rawY) < MOVEMENT_DELTA
                ) {
                    status = true
                }
            }

            status
        }
    }
    private fun View.visible(){
        this.visibility = View.VISIBLE
    }
    private fun View.gone(){
        this.visibility = View.GONE
    }

    companion object {
        const val DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000
        private const val MOVEMENT_DELTA = 10
        fun create(parent: ViewGroup): MessageViewHolder {
            val binding: ListItemMessageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_message,
                parent,
                false
            )
            return MessageViewHolder(binding)
        }
    }
}