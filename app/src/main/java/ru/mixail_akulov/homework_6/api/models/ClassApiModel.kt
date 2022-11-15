package ru.mixail_akulov.homework_6.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClassApiModel (
    @SerializedName("id") @Expose
    var id: Int? = null,

    @SerializedName("name") @Expose
    var name: String? = null

): Comparable<ClassApiModel> {

    override fun compareTo(other: ClassApiModel): Int {
        val parseName = name?.split(" ")
        val parseOtherName = other.name?.split(" ")

        val compareName: Int = parseName(parseName)
        val compareOtherName: Int = parseName(parseOtherName)

        return if (name != other.name) compareOtherName - compareName
        else compareName.compareTo(compareOtherName)
    }

    private fun parseName(parseName: List<String>?): Int {
        return if (parseName!![1] == "кВ") parseName[0].toInt() * 1000
        else parseName[0].toInt()
    }
}