package com.example.admint2t

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/api/login/")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/api/register/")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    // You can define more endpoints here as needed
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val access: String, val refresh: String)
data class RegisterRequest(val username: String, val email: String, val password: String, val user_type: String)
data class RegisterResponse(val username: String, val email: String, val user_type: String)
