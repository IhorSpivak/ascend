package com.doneit.ascend.presentation.video_chat.states

enum class VideoChatState {
    PREVIEW,
    PREVIEW_DATA_LOADED, //still preview screen, but group data available for setup
    PREVIEW_GROUP_STARTED, //current user is mm and see screen preview but time to start has come
    MM_CONNECTION_LOST, //current user is mm and he has lost internet connection
    PROGRESS,
    FINISHED
}