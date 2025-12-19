package com.example.kotlintest.util.data

import com.example.kotlintest.util.Const
import com.example.kotlintest.util.data.model.MainResources
import com.example.kotlintest.util.data.model.MainResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

open class BaseUseCase<I, T> {

    operator fun invoke(i: I): Flow<MainResources<T>> = flow {

        try {
            // Emit loading state
            emit(MainResources.isLoading())

            // API call
            var response = onCall(i)

            // Emit success
            emit(MainResources.Sucess(data = response.data))

        } catch (e: HttpException) {

            var res= (e as? HttpException)?.response()?.errorBody()?.string()

            if (res != null) {
                try {
                    val jObjError = JSONObject(res)

                    if (jObjError.getInt("status") == 401) {
                        emit(
                            MainResources.isUnAuthorized(
                                message = jObjError.getString("message") ?: ""
                            )
                        )
                    } else {
                        val errors = Const.convertJsonToMap(jObjError.getString("errors"))
                        val messageMap = Const.getAllValuesAsString(errors)
                        val message = Const.getAllValuesConcatenated(messageMap)

                        emit(MainResources.isError(message = message))
                    }

                } catch (ex: Exception) {
                    emit(MainResources.isError(message = ex.message ?: ""))
                }

            } else {
                emit(MainResources.isError(message = "no internet"))
            }

        } catch (e: IOException) {
            emit(MainResources.isError(message = "no internet"))
        }

    }.flowOn(Dispatchers.IO)

    open suspend fun onCall(i: I): MainResponse<T> {
        TODO()
    }
}
