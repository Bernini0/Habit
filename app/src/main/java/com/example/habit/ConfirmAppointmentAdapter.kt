package com.example.habit


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference.CompletionListener
import com.google.firebase.database.ktx.getValue


class ConfirmAppointmentAdapter(names: MutableList<PatientInfo>, context: Context, _bundle: Bundle) :
    RecyclerView.Adapter<ConfirmAppointmentAdapter.ViewHolder>() {
    var names: MutableList<PatientInfo>
    var context: Context
    var bundle: Bundle

    init {
        this.names = names
        this.context = context
        bundle = _bundle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.confirm_appointment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = names[position].patientName
        holder.emailTextView.text = names[position].patientEmail
        val uId = names[position].uId;
        val pp = names[position].position
        holder.button.setOnClickListener {
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (i in dataSnapshot.children){
                        val appointmentInfo = i.getValue<AppointmentInfo>()
                        if(appointmentInfo?.id == bundle.get("myUid").toString()){
                            FirebaseDatabase.getInstance().reference.child("user").child(uId).child("appointment").child(i.key.toString()).child("accepted").setValue(true).addOnSuccessListener {
                                Log.d("ConfirmAppointment", "appointment accepted")
                                Toast.makeText(context, "Appointment Approved, Now this user can send you text messages",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("ConfirmAppointment", "loadPatientList:onCancelled", databaseError.toException())
                }
            }
            FirebaseDatabase.getInstance().reference.child("user").child(uId).child("appointment").addListenerForSingleValueEvent(userListener)
            FirebaseDatabase.getInstance().reference.child("user").child(bundle.get("myUid") as String).child("appointment").child(pp).child("accepted").setValue(true).addOnSuccessListener {
                Log.d("ConfirmAppointment", "appointment accepted")
            }
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }

    fun filterList(filterlist: MutableList<PatientInfo>) {
        names = filterlist
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var button: Button
        var emailTextView: TextView
        init {
            titleTextView = itemView.findViewById(R.id.patientName)
            button = itemView.findViewById(R.id.confirmAppointmentButton)
            emailTextView = itemView.findViewById(R.id.patientEmail)
        }
    }
}
