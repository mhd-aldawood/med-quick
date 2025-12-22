package com.example.kotlintest.util.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type
import javax.inject.Inject

class SharedPreferanceRepositoryImpl @Inject constructor(
    private val preferance:SharedPreferences ,
    private val gson: Gson
):SharedPreferanceRepository{
    override fun saveString(key: String, value: String) {
        preferance.edit().putString(key,value).apply()
    }

    override fun getString(key: String, defultValue: String): String {
        return preferance.getString(key,defultValue) ?: defultValue
    }

    override fun saveInt(key: String, value: Int) {
        preferance.edit().putInt(key,value).apply()
    }

    override fun getInt(key: String, defultValue: Int): Int {
        return preferance.getInt(key,defultValue)
    }

    override fun saveBoolean(key: String, value: Boolean) {
        preferance.edit().putBoolean(key,value).apply()
    }

    override fun getBoolean(key: String, defultValue: Boolean): Boolean {
        return preferance.getBoolean(key,defultValue)
    }

    override fun <T> saveObjectToSharedPreferences(key: String, obj: T) {
        val serializedObject = gson.toJson(obj)
        val editor = preferance.edit()
        editor.putString(key,serializedObject)
        editor.apply()
    }

    override fun <T> getObjectFromSharedPreferences(key: String, type: Type): T? {
        val serializedObject = preferance.getString(key,null)
        return gson.fromJson(serializedObject,type)
    }

    override fun remove(key: String) {
        val editor = preferance.edit()
        editor.remove(key)
        editor.apply()
    }
}