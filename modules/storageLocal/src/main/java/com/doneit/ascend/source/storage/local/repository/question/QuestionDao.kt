package com.doneit.ascend.source.storage.local.repository.question

import androidx.room.*
import com.doneit.ascend.source.storage.local.data.AnswerOptionLocal
import com.doneit.ascend.source.storage.local.data.QuestionLocal
import com.doneit.ascend.source.storage.local.data.QuestionWithAnswerOptions

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questions: QuestionLocal)

    @Transaction @Query("SELECT * FROM questions")
    suspend fun getAll(): List<QuestionWithAnswerOptions>

    @Transaction @Query("DELETE FROM questions")
    suspend fun removeAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(answerOption: AnswerOptionLocal)

    @Transaction
    suspend fun insertAll(questions: List<QuestionLocal>) {
        questions.forEach {
            insert(it)

            if (it.options.isNotEmpty()) {
                it.options.forEach { answerOptions ->
                    insert(answerOptions)
                }
            }
        }
    }
}