package com.example.kotlintest.core.devicesWorker

import org.json.JSONObject

interface Worker {
    fun startWork(result: (JSONObject) -> Unit)
    fun stopWork()
}