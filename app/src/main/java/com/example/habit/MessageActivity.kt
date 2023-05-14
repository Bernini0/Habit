package com.example.habit



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habit.databinding.ActivityMessageBinding
import com.example.habit.databinding.LeftItemLayoutBinding
import com.example.habit.databinding.RightItemLayoutBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*

class MessageActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding: ActivityMessageBinding
    private var myUId: String? = null
    private var hisUId: String? = null
    private var chatUId: String? = null
    private var hisUsername: String? = null
    private var util: Util? = null
    private var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<MessageModel, ViewHolder>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.activity_message,
            null,
            false
        )
        setContentView(binding!!.root)
        util = Util()
        myUId = util!!.uId
        hisUId = intent.getStringExtra("hisUId")
        hisUsername = intent.getStringExtra("hisUsername")
        (findViewById<View>(R.id.chatName)as TextView).text = hisUsername

        hisUsername?.let { Log.d("MessageActivity", it) }
//        set()
        binding!!.btnSend.setOnClickListener {
            val message: String = binding!!.msgText.text.toString().trim()
            if (message.isEmpty()) {
                Toast.makeText(this@MessageActivity, "Enter Message", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(message)
                (binding!!.msgText).text.clear()
            }
            util!!.hideKeyboard(this@MessageActivity)
        }
        if (chatUId == null) {
            checkchat(hisUId)
        }
    }

    private fun set() {
//        binding.toolbar.recipientUsername.setText(hisUsername)
//        Log.d("MessageActivity", binding.toolbar.recipientUsername.getText().toString())
    }

    private fun checkchat(hisUId: String?) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("user").child("ChatList").child(myUId!!)
        val query: Query = databaseReference.orderByChild("member").equalTo(hisUId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    for (ds in snapshot.children) {
                        val id = ds.child("member").value.toString()
                        if (id == hisUId) {
                            chatUId = ds.key
                            readMessages(chatUId)
                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun createChat(msg: String) {
        var databaseReference =
            FirebaseDatabase.getInstance().getReference("user").child("ChatList").child(
                myUId!!
            )
        chatUId = databaseReference.push().key
        val chatListModel = ChatListModel(chatUId, util!!.currentData(), msg, hisUId)
        databaseReference.child(chatUId!!).setValue(chatListModel)
        databaseReference =
            FirebaseDatabase.getInstance().getReference("user").child("ChatList").child(
                hisUId!!
            )
        //        chatUId = databaseReference.push().getKey();
        val chatListModel1 = ChatListModel(chatUId, util!!.currentData(), msg, myUId)
        databaseReference.child(chatUId!!).setValue(chatListModel1)
        databaseReference =
            FirebaseDatabase.getInstance().getReference("user").child("chat").child(
                chatUId!!
            )
        val messageModel =
            MessageModel(myUId, hisUId, msg, util!!.currentData(), "text", hisUsername)
        databaseReference.push().setValue(messageModel)
        readMessages(chatUId)
    }

    private fun sendMessage(msg: String) {
        if (chatUId == null) {
            createChat(msg)
        } else {
            val messageModel =
                MessageModel(myUId, hisUId, msg, util!!.currentData(), "text", hisUsername)
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("user").child("chat").child(
                    chatUId!!
                )
            databaseReference.push().setValue(messageModel)
        }
    }

    private fun readMessages(chatUId: String?) {
        val query: Query = FirebaseDatabase.getInstance().getReference("user").child("chat").child(
            chatUId!!
        )
        val options = FirebaseRecyclerOptions.Builder<MessageModel>()
            .setQuery(query,MessageModel::class.java)
            .build()


        firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<MessageModel, ViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return if (viewType == 0) {
                    val viewDataBinding: ViewDataBinding = RightItemLayoutBinding.inflate(
                        LayoutInflater.from(
                            baseContext
                        ), parent, false
                    )
                    ViewHolder(viewDataBinding)
                } else {
                    val viewDataBinding: ViewDataBinding = LeftItemLayoutBinding.inflate(
                        LayoutInflater.from(
                            baseContext
                        ), parent, false
                    )
                    ViewHolder(viewDataBinding)
                }
            }

            override fun onBindViewHolder(
                holder: ViewHolder,
                position: Int,
                model: MessageModel
            ) {
                holder.viewDataBinding.setVariable(BR.message, model)
            }

            override fun getItemViewType(position: Int): Int {
                val messageModel: MessageModel = getItem(position)
                return if (myUId == messageModel.sender) {
                    0
                } else 1
            }
        }
        binding?.recyclerViewMessage?.layoutManager = LinearLayoutManager(this)
        binding?.recyclerViewMessage?.setHasFixedSize(false)
        binding?.recyclerViewMessage?.adapter = firebaseRecyclerAdapter
        (firebaseRecyclerAdapter as FirebaseRecyclerAdapter<MessageModel, ViewHolder>).startListening()
    }

    inner class ViewHolder(viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        val viewDataBinding: ViewDataBinding

        init {
            this.viewDataBinding = viewDataBinding
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}