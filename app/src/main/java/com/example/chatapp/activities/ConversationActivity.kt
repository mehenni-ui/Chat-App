package com.example.chatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.adapters.ChatAdapter
import com.example.chatapp.databinding.ActivityConversationBinding
import com.example.chatapp.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class ConversationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConversationBinding
    private lateinit var sendMessageRef : DatabaseReference
    private lateinit var chatAdapter: ChatAdapter

    private lateinit var messageList : ArrayList<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sendMessageRef = FirebaseDatabase.getInstance().getReference("Chat")
        chatAdapter = ChatAdapter()
        messageList = ArrayList()



        val userId = intent?.getStringExtra("userIdBundle")!!
        val userName = intent?.getStringExtra("userNameBundle")
        val userImage = intent?.getStringExtra("userImageBundle")
        val userIsOnLine = intent?.getBooleanExtra("userIsOnLineBundle", false)

        Log.d("userIdBundle", userId)
        Log.d("userNameBundle", userName!!)
        Log.d("userImageBundle", userImage!!)
        Log.d("userIsOnLineBundle", userIsOnLine!!.toString())
        Log.d("currentUser", Firebase.auth.currentUser!!.uid)
        binding.txtUserNameId.text = userName

        if (userImage == ""){
            binding.imgUserImageId.setImageResource(R.drawable.profile)
        }else{
            Glide.with(this).load(userImage).into(binding.imgUserImageId)
        }

        readMessage(Firebase.auth.currentUser!!.uid, userId)


        binding.messageSenderBtnId.setOnClickListener {
            if (binding.messageSpaceEtId.text.isEmpty()){
                Toast.makeText(this, "you can not send an empty message", Toast.LENGTH_LONG).show()
            }else{
                sendMessage(
                    Firebase.auth.currentUser!!.uid,
                    userId,
                    binding.messageSpaceEtId.text.toString()
                )

                binding.messageSpaceEtId.text.clear()
            }

        }
    }

    private fun sendMessage(senderId: String, receiverId: String, messageContent: String) {
        val message = Message(senderId, receiverId, messageContent)
        sendMessageRef.push().setValue(message)
    }


    private fun readMessage(senderId: String, receiverId: String){

        binding.rvMessageId.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        sendMessageRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (data in snapshot.children){
                    val message = data.getValue(Message::class.java)
                    if (message!!.senderId == senderId && message.receiverId == receiverId || message.senderId == receiverId && message.receiverId ==  senderId){
                        messageList.add(message)
                    }
                }

                chatAdapter.setMessageList(messageList)

                binding.rvMessageId.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}