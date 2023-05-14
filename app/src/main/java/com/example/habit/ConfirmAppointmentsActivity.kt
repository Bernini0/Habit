package com.example.habit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ConfirmAppointmentsActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView : RecyclerView
    private lateinit var patientList : MutableList<PatientInfo>
    private lateinit var adapter : ConfirmAppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        patientList = mutableListOf()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_appointments)
        recyclerView = findViewById(R.id.patientListRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        val DoctorListAdapter = null
        val bundle : Bundle = bundleOf()
        bundle.putString("myUid", mAuth.currentUser?.uid.toString())
        adapter = ConfirmAppointmentAdapter(patientList, this, bundle)
        populate()
        recyclerView.adapter = adapter
    }

    private fun populate(){

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                patientList.clear()
                for (i in dataSnapshot.children){
                    val appointmentInfo = i.getValue<AppointmentInfo>()
                    if(!appointmentInfo!!.accepted){
                        patientList.add(PatientInfo(appointmentInfo.name, i.key.toString(), appointmentInfo.id, appointmentInfo.email))
                        Log.d("ConfirmAppointment", patientList.size.toString())
                    }
                }
                adapter.filterList(patientList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ConfirmAppointment", "loadPatientList:onCancelled", databaseError.toException())
            }
        }
        database.child("user").child(mAuth.currentUser?.uid.toString()).child("appointment").addValueEventListener(userListener)
    }
}