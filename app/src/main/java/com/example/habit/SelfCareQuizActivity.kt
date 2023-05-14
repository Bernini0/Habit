package com.example.habit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SelfCareQuizActivity : AppCompatActivity(), OnItemClickListener {
    private val defaultOptionList = listOf<String>(
        "Strongly Agree",
        "Agree",
        "Neutral",
        "Disagree",
        "Strongly Disagree"
    )
    private val mcqList = listOf<Mcq>(
        Mcq("I tend to bounce back quickly after hard times", defaultOptionList),
        Mcq("I have hard time making it through stressful events", defaultOptionList),
        Mcq("It does not take me long to recover from a stressful event", defaultOptionList),
        Mcq("It is hard for me to snap back when something bad happens", defaultOptionList),
        Mcq("I usually come through difficult times with little trouble", defaultOptionList),
        Mcq("I tend to take a long time to get over set-backs in my life", defaultOptionList),
        Mcq("In general would you say your health is", listOf("Excellent", "Very good", "Good", "Fair", "Poor")),
        Mcq("Please indicate if you have a serious or chronic medical condition", listOf("Asthma", "Diabetes", "Heart disease", "Back or Chronic pain", "None"))
    )

    private var currentIndex = 0
    private var listView: ListView? = null
    private var quesText: TextView? = null
    private var questionPositionText: TextView? = null

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_care_quiz)

        mAuth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        listView = findViewById<ListView>(R.id.self_care_quiz_list_view)
        quesText = findViewById<TextView>(R.id.question_text)
        questionPositionText = findViewById<TextView>(R.id.question_position_text)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mcqList[currentIndex].options)

        listView!!.adapter = adapter
        quesText!!.text = mcqList[currentIndex].question

        val txt = "${currentIndex+1}/${mcqList.size}"
        questionPositionText!!.text = txt

        listView!!.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        listView!!.isEnabled = false
        mcqList[currentIndex].selected = position

        if (currentIndex+1 < mcqList.size) {
            currentIndex++

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mcqList[currentIndex].options)

            listView!!.adapter = adapter
            quesText!!.text = mcqList[currentIndex].question

            val txt = "${currentIndex+1}/${mcqList.size}"
            questionPositionText!!.text = txt

            listView!!.isEnabled = true
        } else {
            calculate()
        }
    }

    private fun calculate() {
        val tot = mcqList.sumOf { it.selected+1 }
        val avg = tot / mcqList.size


        val user = mAuth.currentUser
        if (user == null) {
            return
        }

        database.child("self_care_points").child(user.uid).setValue(avg)

        val intent = Intent(this, SelfCareHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

//    private class MyArrayAdapter private constructor(
//        private val mContext: Context,
//        resource: Int,
//        private val mOptions: Array<String>
//    ) :
//        ArrayAdapter<Class<*>?>(mContext, resource, mOptions) {
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            var view = convertView
//            if (convertView == null) {
//                val inflater = mContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//                view = inflater.inflate(android.R.layout.simple_list_item_1, null)
//            }
//            (view!!.findViewById<View>(android.R.id.text1) as TextView).text = mOptions[position]
//            return view
//        }
//    }
}