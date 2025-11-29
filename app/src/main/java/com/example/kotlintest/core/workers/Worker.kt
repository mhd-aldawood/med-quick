package com.example.kotlintest.core.workers

import org.json.JSONObject

interface Worker {
    fun startWork(result: (JSONObject) -> Unit)
    fun stopWork()
}