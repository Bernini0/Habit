package com.example.habit

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URL


class NewsAdapter(names:MutableList<Article>, context: Context, _bundle: Bundle) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    var names: MutableList<Article>
    var context: Context
    var bundle: Bundle

    init {
        this.names = names
        this.context = context
        bundle = _bundle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.news_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.setText(names[position].title)
        holder.websiteTextView.text = names[position].author
        val url = names[position].urlToImage
        if(url!= null ) {
            DownloadImageTask(holder.imageView).execute(names[position].urlToImage)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            searchWeb(names[position].url)
        })
    }


    override fun getItemCount(): Int {
        return names.size
    }

    fun searchWeb(query: String) {
        val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, query)
        }
//        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
//        }
    }
    fun filterList(filterlist: MutableList<Article>) {
        names = filterlist
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var websiteTextView: TextView
        var imageView: ImageView

        init {
            titleTextView = itemView.findViewById(R.id.newsDescription)
            websiteTextView = itemView.findViewById(R.id.websiteName)
            imageView = itemView.findViewById(R.id.newsThumbnail)
        }
    }

    class DownloadImageTask(var bmImage: ImageView) :
        AsyncTask<String?, Void?, Bitmap?>() {
        override fun onPostExecute(result: Bitmap?) {
            bmImage.setImageBitmap(result)
        }

        override fun doInBackground(vararg p0: String?): Bitmap? {
            val urldisplay = p0[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Error", e.message!!)
                e.printStackTrace()
            }
            return mIcon11
        }
    }
}
