package com.example.database

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Интерфейс Retrofit для определения методов API
interface ApiService {

    @POST("Auth") // Аннотация для указания пути запроса (подставьте правильный путь)
    suspend fun sendPhoneNumber(@Body request: AuthRequest): Response<AuthResponse>

}

// Объект для создания экземпляра Retrofit
object ApiClient {

    private const val BASE_URL = "https://lk.pravoe-delo.su/api/documentation"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
