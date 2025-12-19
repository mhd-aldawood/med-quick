package com.example.kotlintest.util.data.local

import java.lang.reflect.Type

interface SharedPreferanceRepository {
    fun saveString(key:String,value:String)
    fun getString(key:String,defultValue:String):String
    fun saveInt(key:String,value: Int)
    fun getInt(key:String,defultValue: Int):Int
    fun saveBoolean(key:String,value: Boolean)
    fun getBoolean(key: String,defultValue: Boolean):Boolean
    fun <T> saveObjectToSharedPreferences(key: String, obj: T)
    fun <T> getObjectFromSharedPreferences(key: String, type: Type): T?
    fun remove(key:String)
}