package com.example.habit

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("v2/top-headlines?country=us&category=health&apiKey=52daf376b050456d9a1f8fbe121d0cf4")
    fun getData(): Call<responseDataClass>
}