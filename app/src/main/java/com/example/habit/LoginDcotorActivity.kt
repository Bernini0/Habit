package com.example.habit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginDcotorActivity : BaseActivity(), View.OnClickListener {

    private var mEdtEmail: EditText? = null
    private var mEdtPassword: EditText? = null
    private var mLayoutEmail: TextInputLayout? = null
    private var mLayoutPassword: TextInputLayout? = null
    private var mAuthErrorMessage: TextView? = null

    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_dcotor)

        findViewById<View>(R.id.login_doctor_button).setOnClickListener(this)
        findViewById<View>(R.id.go_to_doctor_signup_text).setOnClickListener(this)

        mEdtEmail = findViewById(R.id.login_doctor_email_field)
        mEdtPassword = findViewById(R.id.login_doctor_password_field)
        mLayoutEmail = findViewById(R.id.layout_login_email)
        mLayoutPassword = findViewById(R.id.layout_login_password)
        mAuthErrorMessage = findViewById(R.id.auth_doctor_error_text)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d("auth", "onAuthStateChanged:signed_in:" + user.uid)
                startActivity(Intent(this, DoctoreHomeActivity::class.java))
            } else {
                Log.d("auth", "onAuthStateChanged:signed_out")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.login_doctor_button -> {
                signIn(mEdtEmail!!.text.toString(), mEdtPassword!!.text.toString())
            }
            R.id.go_to_doctor_signup_text -> {
                val intent = Intent(this, SignupDoctorActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)

                finish()
            }
        }
    }

    private fun signIn(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        showProgressDialog()
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this
        ) { task ->
            Log.d(
                "auth",
                "signInWithEmail:onComplete:" + task.isSuccessful
            )
            if (!task.isSuccessful) {
                mAuthErrorMessage?.setText(task.exception!!.message)
                mAuthErrorMessage?.visibility = View.VISIBLE
            }
            hideProgressDialog()
        }
    }

    private fun validateForm(): Boolean {
        return if (TextUtils.isEmpty(mEdtEmail?.getText().toString())) {
            mLayoutEmail?.error = "Required."
            false
        } else if (TextUtils.isEmpty(mEdtPassword?.getText().toString())) {
            mLayoutPassword?.error = "Required."
            false
        } else {
            mLayoutEmail?.error = null
            mLayoutPassword?.error = null
            true
        }
    }
}