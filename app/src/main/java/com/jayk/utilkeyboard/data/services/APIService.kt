package com.jayk.utilkeyboard.data.services

import com.jayk.utilkeyboard.data.models.request.ChatRequest
import com.jayk.utilkeyboard.data.models.response.ChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type:application/json"
    )
    @POST("/chat/completions")
    suspend fun getSearchResults(
        @Header("Authorization") authHeader:String,
        @Body request: ChatRequest
    ):ChatResponse

}