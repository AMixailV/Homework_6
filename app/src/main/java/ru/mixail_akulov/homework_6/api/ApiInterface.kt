package ru.mixail_akulov.homework_6.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.mixail_akulov.homework_6.api.models.ClassApiModel
import ru.mixail_akulov.homework_6.api.models.EquipmentApiModel
import ru.mixail_akulov.homework_6.api.models.TypeApiModel

interface ApiInterface {

    @FormUrlEncoded
    @POST("insertClass.php")
    fun insertClass(
        @Field("name") name: String?,
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("insertType.php")
    fun insertType(
        @Field("name") name: String?
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("insertEquipment.php")
    fun insertEquipment(
        @Field("name") name: String?,
        @Field("class") classU: String?,
        @Field("type") type: String?,
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("updateClass.php")
    fun updateClass(
        @Field("id") id: Int,
        @Field("name") name: String?,
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("updateType.php")
    fun updateType(
        @Field("id") id: Int,
        @Field("name") name: String?,
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("updateEquipment.php")
    fun updateEquipment(
        @Field("id") id: Int,
        @Field("name") name: String?,
        @Field("class") classU: String?,
        @Field("type") type: String?,
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("deleteClass.php")
    fun deleteClass(
        @Field("id") id: Int,
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("deleteType.php")
    fun deleteType(
        @Field("id") id: Int,
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("deleteEquipment.php")
    fun deleteEquipment(
        @Field("id") id: Int,
    ): Call<ResponseBody?>?

    @DELETE("clearClasses.php")
    fun clearClasses(): Call<ResponseBody?>?

    @DELETE("clearTypes.php")
    fun clearTypes(): Call<ResponseBody?>?

    @DELETE("clearEquipments.php")
    fun clearEquipments(): Call<ResponseBody?>?

    @GET("getClasses.php")
    fun getClasses(): Call<ArrayList<ClassApiModel>>

    @GET("getTypes.php")
    fun getTypes(): Call<ArrayList<TypeApiModel>>

    @GET("getEquipments.php")
    fun getEquipments(): Call<ArrayList<EquipmentApiModel>>

    @GET("getEquipmentsFilter.php")
    fun getEquipmentsFilter(@Query("class") classU: String, @Query("type") type: String):
            Call<ArrayList<EquipmentApiModel>>
}