package com.example.habit

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class Util {
    private var username: String? = null
    val uId: String?
        get() {
            val firebaseAuth = FirebaseAuth.getInstance()
            return firebaseAuth.uid
        }

    fun getUsername(uId: String?): String? {
        findUsername(uId)
        return username
    }

    fun findUsername(uId: String?) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("User").child(uId!!)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                username = user?.name
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun currentData(): String {
        val calendar = Calendar.getInstance()
        return sdf().format(calendar.timeInMillis)
    }

    private fun sdf(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd hh-mm-ss a", Locale.US)
    }

    fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    } //    public Bundle putInBundle(String username, String UId){
    //
    //    }
}