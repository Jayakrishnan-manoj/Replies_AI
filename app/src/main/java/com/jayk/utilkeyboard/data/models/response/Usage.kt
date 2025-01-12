package com.jayk.utilkeyboard.data.models.response

data class Usage(
    val completion_tokens: Int,
    val completion_tokens_details: CompletionTokensDetails,
    val prompt_tokens: Int,
    val total_tokens: Int
)