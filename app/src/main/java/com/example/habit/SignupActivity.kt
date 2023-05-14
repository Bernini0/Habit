package com.example.habit

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : BaseActivity(), View.OnClickListener {
    private var mEdtEmail: EditText? = null
    private var mEdtPassword: EditText? = null
    private var mEdtName: EditText? = null
    private var mLayoutEmail: TextInputLayout? = null
    private var mLayoutPassword: TextInputLayout? = null
    private var mLayoutName: TextInputLayout? = null
    private var mAuthErrorMessage: TextView? = null

    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        findViewById<View>(R.id.signup_button).setOnClickListener(this)
        findViewById<View>(R.id.go_to_login_text).setOnClickListener(this)

        mEdtEmail = findViewById(R.id.signup_email_field)
        mEdtPassword = findViewById(R.id.signup_password_field)
        mEdtName = findViewById(R.id.signup_name_field)

        mLayoutEmail = findViewById(R.id.layout_signup_email)
        mLayoutPassword = findViewById(R.id.layout_signup_password)
        mLayoutName = findViewById(R.id.layout_signup_name)
        mAuthErrorMessage = findViewById(R.id.auth_error_text)

        database = Firebase.database.reference

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d("auth", "onAuthStateChanged:signed_in:" + user.uid)

                val usr = User(user.uid, mEdtName!!.text.toString(), mEdtEmail!!.text.toString(), "", "user", "", 0.0, mutableListOf())
                database.child("user").child(user.uid).setValue(usr)

                startActivity(Intent(this, MainActivity::class.java))
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
            R.id.signup_button -> {
                createAccount(mEdtEmail!!.text.toString(), mEdtPassword!!.text.toString())
            }
            R.id.go_to_login_text -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

                startActivity(intent)
                finish()
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        showProgressDialog()
        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task ->
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