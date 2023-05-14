package com.example.habit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.os.bundleOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class PatientActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var uId: String
    private lateinit var usernameTextView: TextView
    private lateinit var useremailTextView: TextView
    private lateinit var userAgeTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)

        var bundle: Bundle? = intent.extras
        uId = bundle!!.get("hisUId") as String
        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        usernameTextView = findViewById<TextView>(R.id.nameOfPatient)
        useremailTextView = findViewById<TextView>(R.id.patientEmail)
        userAgeTextView = findViewById<TextView>(R.id.patientAge)


        getData()
    }

    private fun getData() {
        database.child("user").child(uId).get().addOnSuccessListener {
            val i = it.getValue<User>()

            if(i!= null){
                usernameTextView.text = i.name
                useremailTextView.text = i.email
                if(i.userType=="doctor"){
                    usernameTextView.text = i.name + "(" + i.designation+")"
                    userAgeTextView.text = "Age: 44"
                }
                else{
                    userAgeTextView.text = "Age: 22"
                }
            }
        }
    }


}