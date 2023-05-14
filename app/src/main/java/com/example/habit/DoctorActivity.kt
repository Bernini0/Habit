package com.example.habit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

class DoctorActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView : RecyclerView
    private lateinit var doctorList : MutableList<AppointmentInfo>
    private lateinit var adapter : DoctorListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        doctorList = mutableListOf()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        recyclerView = findViewById(R.id.doctorListRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
//        val DoctorListAdapter = null
        val bundle : Bundle = bundleOf()
        adapter = DoctorListAdapter(doctorList, this, bundle)
        populate()
        recyclerView.adapter = adapter
    }
    
    private fun populate(){

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                doctorList.clear()
                for (i in dataSnapshot.children){
                    val user = i.getValue<User>()
                    if(user!!.userType.equals("doctor")){
                        val appointmentInfo : AppointmentInfo = AppointmentInfo(user.id, user.name + ", "+user.designation, user.email, false)
                        doctorList.add(appointmentInfo)
                        Log.d("DoctorActivity", doctorList.size.toString())
                    }
                }
                adapter.filterList(doctorList)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("DoctorActivity", "loadDoctorList:onCancelled", databaseError.toException())
            }
        }
        database.child("user").addValueEventListener(userListener)
    }
    
    
    
    
    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}
