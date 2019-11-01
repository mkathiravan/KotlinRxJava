package net.kathir.kotlinrxjava

import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST

import retrofit2.http.Part

interface RequestInterface {

    @GET("api/android")
    fun getData():Observable<List<AndroidInfo>>

    @Multipart
    @POST("/images/upload")
    abstract fun uploadImage(@Part image: MultipartBody.Part): Call<Response>
}