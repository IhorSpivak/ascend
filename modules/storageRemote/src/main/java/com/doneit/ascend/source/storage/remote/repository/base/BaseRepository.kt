package com.doneit.ascend.source.storage.remote.repository.base

import androidx.annotation.VisibleForTesting
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Deferred
import retrofit2.Response

abstract class BaseRepository (
    private val gson: Gson
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    suspend fun <T, E> execute(call: (() -> Deferred<Response<T>>), expectedErrorType: Class<E>): RemoteResponse<T, E> {
        val result = call.invoke().await()
        return result.toRemoteResponse(expectedErrorType)
    }

    private fun <T, E> Response<T>.toRemoteResponse(errorType: Class<E>): RemoteResponse<T, E> {
        var error: E? = null
        try {
            error = gson.fromJson(errorBody()?.string(), errorType)
        } catch (exception: JsonSyntaxException){
            exception.printStackTrace()
        }

        return RemoteResponse(
            isSuccessful,
            code(),
            message(),
            body(),
            error
        )
    }
}