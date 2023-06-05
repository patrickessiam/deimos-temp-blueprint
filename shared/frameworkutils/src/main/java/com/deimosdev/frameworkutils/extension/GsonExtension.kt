package com.deimosdev.frameworkutils.extension

import com.google.gson.GsonBuilder

val gson = GsonBuilder().setPrettyPrinting().create()

inline fun <reified T> String.fromJson(): T? {
    return try {
        gson.fromJson(this, T::class.java)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> T.toJson(): String {
    return gson.toJson(this)
}

inline fun <reified T> String.fromJsonList(): List<T>? {
    return try {
        gson.fromJson(this, Array<T>::class.java).toList()
    } catch (e: Exception) {
        null
    }
}
