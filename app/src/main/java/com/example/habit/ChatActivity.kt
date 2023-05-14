package com.example.habit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var database: DatabaseReference
    private lateinit var names : MutableList<AppointmentInfo>
    private lateinit var adapter : ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        names = mutableListOf()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var recyclerView: RecyclerView = findViewById(R.id.chatRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(names,this)
        recyclerView.adapter = adapter
        populate()
    }

    fun populate() {
        val databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("user").child(mAuth.currentUser?.uid.toString()).child("appointment")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    names.clear()
                    for (dataSnapshot in snapshot.children) {
                        val appointmentInfo: AppointmentInfo? = dataSnapshot.getValue(AppointmentInfo::class.java)
                        if(appointmentInfo?.accepted == true){
                            names.add(appointmentInfo)
                            adapter.filterList(names)
                        }
                    }
                    //                adapter.filterList(names);
                    Log.d("ChatFragmentfFriendsize", names.size.toString())
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}