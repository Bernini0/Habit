package com.example.habit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class DoctoreHomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctore_home)

        mAuth = FirebaseAuth.getInstance()

        findViewById<View>(R.id.confirmAppointment).setOnClickListener(this)
        findViewById<View>(R.id.patientMessages).setOnClickListener(this)
        findViewById<View>(R.id.doctorLogout).setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.confirmAppointment ->{
                intent = Intent(this, ConfirmAppointmentsActivity::class.java)
                startActivity(intent)
            }
            R.id.patientMessages ->{
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }
            R.id.doctorLogout->{
                mAuth.signOut()
                val intent = Intent(applicationContext, LoginOnboardingActivity::class.java)
                startActivity(intent);
                finish()
            }

        }
    }
}