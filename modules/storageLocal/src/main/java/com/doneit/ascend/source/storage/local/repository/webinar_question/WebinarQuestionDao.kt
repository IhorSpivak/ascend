package com.doneit.ascend.source.storage.local.repository.webinar_question

import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.WebinarQuestionLocal

@Dao
interface WebinarQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questions: WebinarQuestionLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questionItems: List<WebinarQuestionLocal>)

    @Query("SELECT * FROM webinar_questions ORDER BY updatedAt DESC")
    fun getAll(): DataSource.Factory<Int, WebinarQuestionLocal>

    @Transaction
    @Query("DELETE FROM webinar_questions")
    suspend fun removeAll()
}