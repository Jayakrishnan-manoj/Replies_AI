package com.jayk.utilkeyboard.data.models.response

data class ChatResponse(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val service_tier: String,
    val system_fingerprint: String,
    val usage: Usage
)