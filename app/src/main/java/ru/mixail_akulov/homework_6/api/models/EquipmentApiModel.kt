package ru.mixail_akulov.homework_6.api.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EquipmentApiModel (
    @SerializedName("id") @Expose
    var id: Int? = null,

    @SerializedName("name") @Expose
    var name: String? = null,

    @SerializedName("class") @Expose
    var classU: String? = null,

    @SerializedName("type") @Expose
    var type: String? = null
)