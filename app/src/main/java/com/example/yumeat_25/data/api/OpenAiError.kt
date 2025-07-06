package com.example.yumeat_25.data.api

data class OpenAIError(
    val error: ErrorDetails
)

data class ErrorDetails(
    val message: String,
    val type: String,
    val param: String?,
    val code: String
)