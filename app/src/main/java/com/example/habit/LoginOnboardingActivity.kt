package com.example.habit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LoginOnboardingActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login_onboarding)

        findViewById<View>(R.id.onboard_login_button).setOnClickListener(this)
        findViewById<View>(R.id.onboard_login_doctor).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.onboard_login_button -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.onboard_login_doctor -> {
                startActivity(Intent(this, LoginDcotorActivity::class.java))
            }
        }
    }
}