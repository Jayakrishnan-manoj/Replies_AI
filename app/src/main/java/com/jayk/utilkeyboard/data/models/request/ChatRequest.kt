package com.jayk.utilkeyboard.data.models.request

data class ChatRequest(
    val messages: List<Message>,
    val model: String
)