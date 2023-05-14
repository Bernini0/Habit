package com.example.habit

import android.util.Log
import com.google.firebase.database.*

class User(
    val id: String,
    val name: String,
    val email: String,
    val designation: String,
    val userType: String,
    val bio: String,
    val rating: Double,
    val appointment : MutableList<AppointmentInfo>,
) {
    constructor(): this("", "", "", "", "", "", 0.0, mutableListOf())

    fun addAppointment(user: User) {
//            friends.add(new UsernameAndUId(user.uId, user.username));
        val mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("user").child(this.id).child("appointment").orderByChild("id")
            .equalTo(user.id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount > 0) {
                        Log.d("User", "works")
                    } else {
                        appointment.add(AppointmentInfo(user.id, user.name,user.email, false))
                        mDatabase.child("user").child(id).child("appointment").setValue(appointment)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}