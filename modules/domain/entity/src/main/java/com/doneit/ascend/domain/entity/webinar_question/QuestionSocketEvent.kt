package com.doneit.ascend.domain.entity.webinar_question

enum class QuestionSocketEvent(
    val command: String
) {
    CREATE("create"),
    UPDATE("destroy"),
    DESTROY("read");

    override fun toString(): String {
        return command
    }

    companion object {
        fun fromRemoteString(representation: String): QuestionSocketEvent {
            return QuestionSocketEvent.values()
                .firstOrNull { it.command == representation } ?: CREATE
        }
    }
}