package com.example.habit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth

class DoctoreHomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctore_home)

        mAuth = FirebaseAuth.getInstance()

        val toolbar = findViewById<View>(R.id.toolbar2)
        val spinner = findViewById<View>(R.id.spinner2) as Spinner

        val logout = arrayOf("Home","Feedback","Logout")

        //values in the spinner
        val adapter = ArrayAdapter(this, R.layout.spinner_item, logout)

        //setting adapter to spinner
        spinner.adapter = adapter

        //spinner item selection
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                when(position) {
                    0 ->{
                        //do nothing
                    }
                    2 -> {
                        mAuth.signOut()
                        val intent = Intent(applicationContext, LoginOnboardingActivity::class.java)
                        startActivity(intent);
                        finish()
                    }
                    1 ->{
                        val intent = Intent(applicationContext, FeedbackActivity::class.java)
                        startActivity(intent);
                    }
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        findViewById<View>(R.id.self_care_container34).setOnClickListener(this)
        findViewById<View>(R.id.consultation_card34).setOnClickListener(this)
        findViewById<View>(R.id.chat_card34).setOnClickListener(this)
        findViewById<View>(R.id.button434).setOnClickListener(this)
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
            R.id.button434 ->{
                val intent = Intent(applicationContext, NewsActivity::class.java)
                startActivity(intent);
            }

        }
    }
}