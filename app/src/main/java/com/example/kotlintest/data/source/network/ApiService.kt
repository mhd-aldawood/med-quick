package com.example.kotlintest.data.source.network

import com.example.kotlintest.screens.users.ApiResult
import kotlinx.coroutines.flow.Flow

interface ApiService {
    fun getUsers(): Flow<ApiResult<List<User>>>

}