package com.jayk.utilkeyboard.data.models.response

data class CompletionTokensDetails(
    val accepted_prediction_tokens: Int,
    val reasoning_tokens: Int,
    val rejected_prediction_tokens: Int
)