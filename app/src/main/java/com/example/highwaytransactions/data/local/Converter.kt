package com.example.highwaytransactions.data.local

import androidx.room.TypeConverter
import com.example.highwaytransactions.domain.model.LocalizedString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun fromLocalizedStr(ls: LocalizedString?): String {
        val gson = Gson()
        return gson.toJson(ls)
    }

    @TypeConverter
    fun toLocalizedStr(data: String?): LocalizedString {
        val gson = Gson()
        val ls = object : TypeToken<LocalizedString>() {}.type
        return gson.fromJson(data, ls)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(data: String?): List<String> {
        val gson = Gson()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType) ?: emptyList()
    }
}