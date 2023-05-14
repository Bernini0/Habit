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

        findViewById<View>(R.id.self_care_container34).setOnClickListener(this)
        findViewById<View>(R.id.consultation_card34).setOnClickListener(this)
        findViewById<View>(R.id.chat_card34).setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.self_care_container34 ->{
                intent = Intent(this, ConfirmAppointmentsActivity::class.java)
                startActivity(intent)
            }
            R.id.consultation_card34 ->{
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }
            R.id.chat_card34->{
                mAuth.signOut()
                val intent = Intent(applicationContext, LoginOnboardingActivity::class.java)
                startActivity(intent);
                finish()
            }

        }
    }
}