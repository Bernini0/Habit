package com.example.habit

data class responseDataClass(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)