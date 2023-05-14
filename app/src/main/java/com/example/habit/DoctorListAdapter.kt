package com.example.habit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class DoctorListAdapter(names: MutableList<AppointmentInfo>, context: Context, _bundle: Bundle) :
    RecyclerView.Adapter<DoctorListAdapter.ViewHolder>() {
    var names: MutableList<AppointmentInfo>
    var context: Context
    var bundle: Bundle

    init {
        this.names = names
        this.context = context
        bundle = _bundle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.docot_name_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.setText(names[position].name)
        holder.emailTextView.text = names[position].email
        val hisUsername = names[position].name
        val hisUid = names[position].id
        val hisEmail = names[position].name
        holder.itemView.setOnClickListener {

            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue<User>()
                    val intent = Intent(context, AppointmentActivity::class.java)
                    bundle.putString("hisUsername", user?.name)
                    bundle.putString("hisUId", hisUid)
                    bundle.putString("hisEmail",hisEmail)
                    bundle.putString("hisDesignation", user?.designation)
                    bundle.putString("hisBio", user?.bio)
                    bundle.putString("hisRating", user?.rating.toString())
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
//                    Log.w("DoctorActivity", "loadDoctorList:onCancelled", databaseError.toException())
                }
            }

            FirebaseDatabase.getInstance().reference.child("user").child(hisUid).addListenerForSingleValueEvent(userListener)

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
