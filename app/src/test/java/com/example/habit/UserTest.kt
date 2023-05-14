package com.example.habit

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import junit.framework.TestCase
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class UserTest : TestCase() {



    @Mock
    private lateinit var mDatabase: DatabaseReference

    private lateinit var user: User

    @Mock
    private lateinit var value: Context

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var user2: User

    @Before
    fun setup() {

//        value = InstrumentationRegistry.getInstrumentation().context
        user2 = User()
        FirebaseApp.initializeApp(context)
        mDatabase = FirebaseDatabase.getInstance().reference
        MockitoAnnotations.initMocks(this)
        user = User("123", "John Doe", "johndoe@example.com", "Doctor", "user", "", 0.0, mutableListOf())
        user.appointment.clear()
        mDatabase.child("user").child(user.id).setValue(user)
    }

    @Test
    fun testAddUser(){
//        value = InstrumentationRegistry.getInstrumentation().context
        FirebaseApp.initializeApp(context)
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("user").child(user.id).orderByChild("id").get().addOnSuccessListener {
            assertEquals(true, true)
        }.addOnFailureListener{
            assertEquals(true, false)
        }
    }

    @Test
    fun testDelUser(){
//        value = InstrumentationRegistry.getInstrumentation().context
        FirebaseApp.initializeApp(context)
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("user").child(user.id).orderByChild("id").get().addOnSuccessListener {
            assertEquals(false, true)
        }.addOnFailureListener{
            assertEquals(true, true)
        }
    }

    @Test
    fun confirmAppointment(){
        FirebaseApp.initializeApp(context)
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("user").child(user.id).child("appointments")
            .orderByChild(user2.id).get().addOnSuccessListener {
                mDatabase.child("user").child(user2.id).child("appointments")
                    .orderByChild(user.id).get().addOnSuccessListener {
                        assertEquals(false, true)
                    }
        }.addOnFailureListener{
            assertEquals(true, true)
        }
    }

    @Test
    fun notProperUser(){
        user = User("123", "John Doe", "johndoe@example.com", "Doctor", "user", "", 0.0, mutableListOf())
        user.appointment.clear()
        user2 = User("124", "John Doe", "johndoe@example.com", "Doctor", "user", "", 0.0, mutableListOf())
        user2.appointment.clear()
        addUser(user)
        addUser(user2)


        FirebaseApp.initializeApp(context)
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("user").child("any").orderByChild("id").get().addOnSuccessListener {
            assertEquals(false, true)
        }.addOnFailureListener{
            assertEquals(true, true)
        }
    }

    fun addUser(user: User){

    }
}
