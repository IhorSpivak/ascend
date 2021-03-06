package com.doneit.ascend.source.storage.local.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.doneit.ascend.source.storage.local.data.*
import com.doneit.ascend.source.storage.local.data.chat.BlockedUserLocal
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal
import com.doneit.ascend.source.storage.local.data.community_feed.CommentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostAttachmentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostLocal
import com.doneit.ascend.source.storage.local.data.converters.ListIntConverter
import com.doneit.ascend.source.storage.local.data.converters.ListLongConverter
import com.doneit.ascend.source.storage.local.data.converters.ListStringConverter
import com.doneit.ascend.source.storage.local.data.converters.MembersConverter
import com.doneit.ascend.source.storage.local.data.first_time_login.AnswerOptionLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionItemLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionListLocal
import com.doneit.ascend.source.storage.local.data.notification.NotificationLocal
import com.doneit.ascend.source.storage.local.data.notification.NotificationOwnerLocal
import com.doneit.ascend.source.storage.local.repository.attachments.AttachmentDao
import com.doneit.ascend.source.storage.local.repository.chats.MyChatsDao
import com.doneit.ascend.source.storage.local.repository.community_feed.CommunityFeedDao
import com.doneit.ascend.source.storage.local.repository.groups.GroupDao
import com.doneit.ascend.source.storage.local.repository.master_minds.MasterMindDao
import com.doneit.ascend.source.storage.local.repository.notification.NotificationDao
import com.doneit.ascend.source.storage.local.repository.question.QuestionDao
import com.doneit.ascend.source.storage.local.repository.user.UserDao
import com.doneit.ascend.source.storage.local.repository.webinar_question.WebinarQuestionDao

@Database(
    entities = [
        AnswerOptionLocal::class,
        QuestionItemLocal::class,
        QuestionListLocal::class,
        CommunityLocal::class,
        UserLocal::class,
        MasterMindLocal::class,
        ImageLocal::class,
        GroupLocal::class,
        OwnerLocal::class,
        NoteLocal::class,
        AttachmentLocal::class,
        NotificationLocal::class,
        NotificationOwnerLocal::class,
        ChatLocal::class,
        MessageLocal::class,
        TagLocal::class,
        MemberLocal::class,
        BlockedUserLocal::class,
        WebinarQuestionLocal::class,
        PostAttachmentLocal::class,
        PostLocal::class,
        CommentLocal::class
    ],
    version = 3
)
@TypeConverters(
    ListIntConverter::class,
    ListStringConverter::class,
    ListLongConverter::class,
    MembersConverter::class
)
internal abstract class LocalDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao
    abstract fun masterMindDao(): MasterMindDao
    abstract fun groupDao(): GroupDao
    abstract fun attachmentDao(): AttachmentDao
    abstract fun notificationDao(): NotificationDao
    abstract fun myChatsDao(): MyChatsDao
    abstract fun webinarQuestionDao(): WebinarQuestionDao
    abstract fun communityFeedDao(): CommunityFeedDao

    companion object {
        const val NAME = "AscendDB"
    }
}