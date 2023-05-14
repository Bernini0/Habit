package com.example.habit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nextActivity: Int

        auth = Firebase.auth

        installSplashScreen()

        setContentView(R.layout.activity_main)

        database = Firebase.database.reference

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser != null){
            Log.d("user profile", "loggedin")

            database.child("user").child(currentUser.uid).get().addOnSuccessListener {
                val usr = it.getValue<User>()
                var type: String = "user"

                if (usr != null) {
                    type = usr.userType
                }

                var intent: Intent = if (type == "doctor") {
                    Intent(this, DoctoreHomeActivity::class.java)
                } else {
                    Intent(this, Home::class.java)
                }

                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        } else {
            Log.d("user profile", "not logged in")
            val intent = Intent(this, LoginOnboardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}