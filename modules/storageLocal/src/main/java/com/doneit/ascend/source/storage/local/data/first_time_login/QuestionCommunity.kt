package com.doneit.ascend.source.storage.local.data.first_time_login

//@Entity(
//    tableName = "questions_community", foreignKeys = (arrayOf(
//        ForeignKey(
//            entity = QuestionListLocal::class,
//            onUpdate = ForeignKey.CASCADE,
//            parentColumns = arrayOf("question_id"),
//            childColumns = arrayOf("question_community_id")
//        )
//    ))
//)
//data class QuestionCommunity(
//    @PrimaryKey
//    @ColumnInfo(fullName = "question_community_id")
//    var id: Long = Random.nextLong(),
//    @ColumnInfo(fullName = "question_id")
//    var questionId: Long? = 0,
//    @ColumnInfo(fullName = "community_id")
//    var communityId: Long? = 0
//)