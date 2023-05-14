package com.example.habit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<View>(R.id.toolbar)
        val spinner = findViewById<View>(R.id.spinner) as Spinner

        val usernameTxt = findViewById<TextView>(R.id.usernameTextView)

        val logout = arrayOf("Home","Logout")

        //values in the spinner
        val adapter = ArrayAdapter(this, R.layout.spinner_item, logout)

        //setting adapter to spinner
        spinner.adapter = adapter
        findViewById<View>(R.id.self_care_container).setOnClickListener(this)
        findViewById<View>(R.id.chat_card).setOnClickListener(this)
        findViewById<View>(R.id.consultation_card).setOnClickListener(this)
        findViewById<View>(R.id.button420).setOnClickListener(this)


        //spinner item selection
        spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                when(position) {
                    0 ->{
                        //do nothing
                    }
                    1 -> {
                        mAuth.signOut()
                        val intent = Intent(applicationContext, LoginOnboardingActivity::class.java)
                        startActivity(intent);
                        finish()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val user = mAuth.currentUser

        if (user != null) {
            database.child("user").child(user.uid).get().addOnSuccessListener {
                val i = it.getValue<User>()

                if( i != null) {
                   usernameTxt.text = i.name
                }
            }
        }
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.self_care_container -> {
                val user = mAuth.currentUser

                if (user == null) {
                    return
                }

                database.child("self_care_points").child(user.uid).get().addOnSuccessListener {
                    val i = it.getValue<Int>()

                    if(i == 0 || i == null) {
                        Log.i("fire_habbit", "value empty")
                        val intent = Intent(this, SelfCareQuizActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Log.i("fire_habbit_Home", "value is not null or zero1")
                        val intent = Intent(this, SelfCareHomeActivity::class.java)
                        startActivity(intent)
                    }
                }.addOnFailureListener{
                    Log.e("fire_habbit", "Error getting data", it)
                }

                //                val intent = Intent(this, SelfCareActivity::class.java)
                //                startActivity(intent)
            }
            R.id.chat_card ->{
//                mAuth.signOut()
//                val intent = Intent(this, LoginOnboardingActivity::class.java)
//                startActivity(intent)
//                finish()
//                Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ChatActivity::class.java);
                startActivity(intent)

            }
            R.id.consultation_card -> {
                val intent = Intent(this, DoctorActivity::class.java)
                startActivity(intent)
            }
            R.id.button420 ->{
                val intent = Intent(this, FeedbackActivity::class.java)
                startActivity(intent)
            }
        }
    }
}