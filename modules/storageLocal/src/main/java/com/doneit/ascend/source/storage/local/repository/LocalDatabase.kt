package com.doneit.ascend.source.storage.local.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.doneit.ascend.source.storage.local.data.*
import com.doneit.ascend.source.storage.local.data.converters.ListIntConverter
import com.doneit.ascend.source.storage.local.data.first_time_login.AnswerOptionLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionItemLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionListLocal
import com.doneit.ascend.source.storage.local.data.notification.NotificationLocal
import com.doneit.ascend.source.storage.local.data.notification.NotificationOwnerLocal
import com.doneit.ascend.source.storage.local.repository.attachments.AttachmentDao
import com.doneit.ascend.source.storage.local.repository.groups.GroupDao
import com.doneit.ascend.source.storage.local.repository.master_minds.MasterMindDao
import com.doneit.ascend.source.storage.local.repository.notification.NotificationDao
import com.doneit.ascend.source.storage.local.repository.question.QuestionDao
import com.doneit.ascend.source.storage.local.repository.user.UserDao

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
        TagLocal::class/*,
        AttendeeLocal::class*/
    ],
    version = 2//todo move down
)
@TypeConverters(ListIntConverter::class)
internal abstract class LocalDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao
    abstract fun masterMindDao(): MasterMindDao
    abstract fun groupDao(): GroupDao
    abstract fun attachmentDao(): AttachmentDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        const val NAME = "AscendDB"
    }
}