package com.example.habit

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference.CompletionListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class AppointmentActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var database: DatabaseReference
    private lateinit var doctorName: String
    private lateinit var doctorId : String
    private lateinit var myName : String
    private lateinit var myEmail : String
    private lateinit var doctorEmail : String
    private lateinit var doctorBio: String
    private lateinit var doctorDesignation: String
    private var doctorRating: String = "0"
    var appointmentAlreadyExist: Boolean = false
    private var rated: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        var bundle: Bundle? = intent.extras

        doctorId = bundle?.get("hisUId").toString()
        doctorName = bundle?.get("hisUsername").toString()
        doctorEmail = bundle?.get("hisEmail").toString()
        doctorDesignation = bundle?.get("hisDesignation").toString()
        doctorBio = bundle?.get("hisBio").toString()

        doctorRating = bundle?.get("hisRating").toString()
        if (doctorRating == "null") {
            doctorRating = "0"
        }

        var nameOfDoctor : TextView = this.findViewById(R.id.nameOfDoctor);
        nameOfDoctor.text = bundle?.get("hisUsername").toString()
        (findViewById<View>(R.id.doctorDesignation)as TextView).text = doctorDesignation
        (findViewById<View>(R.id.doctorSummary)as TextView).text = doctorBio
        (findViewById<View>(R.id.doctorRating)as TextView).text = "Rating: " + doctorRating

        val mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("user").child(doctorId).child("appointment").orderByChild("id")
            .equalTo(mAuth.currentUser?.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    appointmentAlreadyExist = snapshot.childrenCount > 0
                }
                override fun onCancelled(error: DatabaseError) {}
            })

        mDatabase.child("ratings")
            .orderByChild("doctorId").equalTo(doctorId)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.childrenCount >0) {
                        var total = 0.0

                        for ( i in snapshot.children){
                            val d = i.getValue<Rating>()

                            if (d?.userId.equals(mAuth.currentUser?.uid)){
                                rated = true
                            }

                            total += d?.rating ?: 0.0F
                        }

                        val avg = total / snapshot.childrenCount
                        findViewById<TextView>(R.id.doctorRating).text = "Rating: " + avg.toString()

                        if (rated) {
                            findViewById<View>(R.id.ratingBar).visibility = View.INVISIBLE
                            findViewById<View>(R.id.submitRatingButton).visibility = View.INVISIBLE
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        findViewById<View>(R.id.addAppointment).setOnClickListener(this)
        findViewById<View>(R.id.submitRatingButton).setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.addAppointment ->{

                if(appointmentAlreadyExist){
                    Toast.makeText(this, "Already Requested Appointment for this Doctor", Toast.LENGTH_SHORT).show()
                }
                else {
                    val userListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val user = dataSnapshot.getValue<User>()
                            myName = user?.name.toString()
                            myEmail = user?.email.toString()
                            user?.addAppointment(
                                User(
                                    doctorId,
                                    doctorName,
                                    doctorEmail,
                                    "",
                                    "",
                                    "",
                                    0.0,
                                    mutableListOf()
                                )
                            )
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(
                                "DoctorActivity",
                                "loadDoctorList:onCancelled",
                                databaseError.toException()
                            )
                        }
                    }
                    database.child("user").child(mAuth.uid.toString())
                        .addListenerForSingleValueEvent(userListener)
                    val userListener2 = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val user = dataSnapshot.getValue<User>()
                            user?.addAppointment(
                                User(
                                    mAuth.uid.toString(),
                                    myName,
                                    myEmail,
                                    "",
                                    "",
                                    "",
                                    0.0,
                                    mutableListOf()
                                )
                            )
                            Log.d("AppointmentActivity", "appointment added successfully");
                            Toast.makeText(
                                applicationContext,
                                "Successfully Requested Appointment",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
//                        Log.w("DoctorActivity", "loadDoctorList:onCancelled", databaseError.toException())
                        }
                    }
                    database.child("user").child(doctorId)
                        .addListenerForSingleValueEvent(userListener2)
                }

            }
            R.id.submitRatingButton ->{
                val ratingBar = findViewById<View>(R.id.ratingBar)as RatingBar
                val submitButton = findViewById<View>(R.id.submitRatingButton) as Button
                val rating = ratingBar.rating

                val userId = mAuth.currentUser?.uid as String

                val rt =  Rating(userId, doctorId, rating)

                database.child("ratings").child(UUID.randomUUID().toString()).setValue(rt).addOnSuccessListener {
                    // if success
                    ratingBar.visibility = View.INVISIBLE
                    submitButton.visibility = View.INVISIBLE
                }
            }
        }
    }

}