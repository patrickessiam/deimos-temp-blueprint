package com.deimosdev.utils

import androidx.room.TypeConverter
import com.deimosdev.frameworkutils.extension.fromJson
import com.deimosdev.frameworkutils.extension.toJson

class StringListConverter {
    @TypeConverter
    fun toListOfStrings(stringValue: String): List<String>? {
        return stringValue.fromJson()
    }

    @TypeConverter
    fun fromListOfStrings(listOfString: List<String>?): String {
        return listOfString.toJson()
    }
}
