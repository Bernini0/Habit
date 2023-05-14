package com.example.habit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SelfCareHomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var database: DatabaseReference

    private var scoreText: TextView? = null
    private var scoreMeaningText: TextView? = null
    private var takeQuizAgain: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_care_home)

        scoreText = findViewById<TextView>(R.id.score_text)
        scoreMeaningText = findViewById<TextView>(R.id.meaning_text)
        takeQuizAgain = findViewById<Button>(R.id.takeQuizAgain)

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        val user = mAuth.currentUser
        findViewById<View>(R.id.takeQuizAgain).setOnClickListener(this)
        if (user != null) {
            database.child("self_care_points").child(user.uid).get().addOnSuccessListener {
                val i = it.getValue<Int>()

                if(i == 0 || i == null) {
                    Log.i("fire_habbit", "value empty")
                    val intent = Intent(this, SelfCareQuizActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Log.i("fire_habbit_self", "value is not null or zero2")
                    scoreText!!.text = i.toString()
                    scoreMeaningText!!.text = "Normal Resilience: You are able to bounce back from new or recent stressors"
                }

            }.addOnFailureListener{
                Log.e("fire_habbit", "Error getting data", it)
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.takeQuizAgain ->{
                Log.d("takeQuizAgain","Works")
                val intent = Intent(this, SelfCareQuizActivity::class.java)
                startActivity(intent)
            }
        }
    }
}