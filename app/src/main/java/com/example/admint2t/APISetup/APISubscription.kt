package com.example.admint2t.APISetup

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface APISubscription {
    @POST("api/device-token/")
    suspend fun subscribe(
        @Header("Authorization") authHeader: String,
        @Body device_token: DeviceToken
    ): Response<Void>
}

data class DeviceToken(val device_token: String)
