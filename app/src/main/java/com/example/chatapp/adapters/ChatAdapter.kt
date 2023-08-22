package com.example.chatapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatapp.databinding.ReceiverCustomViewMessageBinding
import com.example.chatapp.databinding.SenderCustomViewMessageBinding
import com.example.chatapp.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChatAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val SENDER_MESSAGE = 0
    private val RECEIVER_MESSAGE = 1
    var firebaseUser : FirebaseUser? = null

    private var messageList  = ArrayList<Message>()

    fun setMessageList(messageList: ArrayList<Message>){
        this.messageList = messageList
        notifyDataSetChanged()
    }

    inner class ChatViewHolderSender(
        var binding : SenderCustomViewMessageBinding
        ) : ViewHolder(binding.root)

    inner class ChatViewHolderReceiver(
        var binding : ReceiverCustomViewMessageBinding
    ) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == SENDER_MESSAGE){
            return ChatViewHolderSender(
                SenderCustomViewMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }else{
            return ChatViewHolderReceiver(
                ReceiverCustomViewMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        if (holder is ChatViewHolderSender){
            holder.binding.senderMessageId.text = message.messageContent
        }else if (holder is ChatViewHolderReceiver){
            holder.binding.recieverMessageId.text = message.messageContent
        }
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (messageList[position].senderId == firebaseUser!!.uid){
            SENDER_MESSAGE
        }else{
            RECEIVER_MESSAGE
        }
    }
}