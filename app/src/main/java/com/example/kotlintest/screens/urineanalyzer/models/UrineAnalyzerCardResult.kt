package com.example.kotlintest.screens.urineanalyzer.models

import com.example.kotlintest.core.model.CardResult

data class UrineAnalyzerCardResult(val id:String, val date:String,val cardResult: MutableList<CardResult>)