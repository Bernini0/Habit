package com.example.habit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsActivity : AppCompatActivity() {

    private val API_KEY = "52daf376b050456d9a1f8fbe121d0cf4"

    private lateinit var newsList: MutableList<Article>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        newsList = mutableListOf()
        recyclerView = findViewById(R.id.newsRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val bundle: Bundle = bundleOf()
        adapter = NewsAdapter(newsList, this, bundle)
        recyclerView.adapter = adapter
        recyclerView.hasFixedSize()
        getData()
    }

    private fun getData() {
        RetrofitInstance.apiInterface.getData().enqueue(object : Callback<responseDataClass?> {
            override fun onResponse(
                call: Call<responseDataClass?>,
                response: Response<responseDataClass?>
            ) {
                Log.d("NewsActivity", response.body()!!.totalResults.toString())
                newsList = response.body()!!.articles as MutableList<Article>
                adapter.filterList(newsList)
                Log.d("NewsActivity", "Kam kore Probably")
            }

            override fun onFailure(call: Call<responseDataClass?>, t: Throwable) {
                Log.d("NewsActivity", "Baal Amar")
            }
        })
    }
}