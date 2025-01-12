package com.jayk.utilkeyboard.data.repository

import com.jayk.utilkeyboard.data.ApiResult
import com.jayk.utilkeyboard.data.models.request.Message
import com.jayk.utilkeyboard.data.models.response.ChatResponse

interface APIRepository {
    suspend fun getSearchResults(
        model:String,
        messages:List<Message>
    ) : ApiResult<ChatResponse>
}