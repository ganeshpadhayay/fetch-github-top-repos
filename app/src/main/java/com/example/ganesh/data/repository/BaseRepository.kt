package com.example.ganesh.data.repository

import android.util.Log
import com.example.ganesh.network.NetworkResult
import retrofit2.Response
import java.io.IOException

open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result: NetworkResult<T> = safeApiResult(call, errorMessage)
        var data: T? = null

        when (result) {
            is NetworkResult.Success ->
                data = result.data
            is NetworkResult.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }

        return data
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): NetworkResult<T> {
        val response = call.invoke()
        if (response.isSuccessful) return NetworkResult.Success(response.body()!!)

        return NetworkResult.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}
