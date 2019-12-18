package com.doneit.ascend.source.storage.local.repository.question

import androidx.room.*
import com.doneit.ascend.source.storage.local.data.*

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questions: QuestionListLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionItems(questionItems: List<QuestionItemLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswerOptions(answerOptions: List<AnswerOptionLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunity(community: CommunityLocal)

    @Transaction
    @Query("SELECT * FROM questions")
    suspend fun getQuestions(): QuestionWithAnswerOptions

    @Transaction
    @Query("SELECT * FROM answer_options WHERE community_id == :communityId")
    suspend fun getAllAnswerOptions(communityId: Long): List<AnswerOptionLocal>

    @Transaction
    @Query("DELETE FROM questions")
    suspend fun removeAll()

    @Transaction
    suspend fun insertAll(questions: QuestionListLocal) {

        val questionItems = questions.questionItems.map {
            it.copy(questionId = questions.id)
        }

        insertQuestionItems(questionItems)

        questions.community?.let {

            val answerOptions = it.options.map { optionIt ->
                optionIt.copy(communityId = it.id)
            }

            insertAnswerOptions(answerOptions)

            insertCommunity(it.copy(questionId = questions.id))
        }

        insertQuestion(questions)
    }

    @Transaction
    suspend fun getAllQuestions() : QuestionWithAnswerOptions {
        val question = getQuestions()

        question.community?.let {
            it.options = getAllAnswerOptions(it.id)
        }

        return question
    }
}