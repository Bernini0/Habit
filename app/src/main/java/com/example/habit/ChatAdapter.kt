package com.example.habit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ChatAdapter(names: MutableList<AppointmentInfo>, context: Context) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    var names: MutableList<AppointmentInfo>
    var context: Context

    init {
        this.names = names
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.docot_name_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = names[position].name
        holder.emailTextView.text = names[position].email
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            val bundle = Bundle()
            bundle.putString("hisUsername", names[position].name)
            bundle.putString("hisUId", names[position].id)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }

    fun filterList(filterlist: MutableList<AppointmentInfo>) {
        names = filterlist
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var emailTextView: TextView

        init {
            titleTextView = itemView.findViewById(R.id.doctorName)
            emailTextView = itemView.findViewById(R.id.doctorEmail)
        }
    }
}
