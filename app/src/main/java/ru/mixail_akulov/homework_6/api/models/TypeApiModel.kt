package ru.mixail_akulov.homework_6.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TypeApiModel (
    @SerializedName("id") @Expose
    var id: Int? = null,

    @SerializedName("name") @Expose
    var name: String? = null
)