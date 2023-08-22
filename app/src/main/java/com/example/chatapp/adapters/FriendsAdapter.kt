package com.example.chatapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.databinding.CustomUserViewBinding
import com.example.chatapp.models.User
import kotlin.coroutines.coroutineContext

class FriendsAdapter : RecyclerView .Adapter<FriendsAdapter.FriendViewHolder>(){


    inner class FriendViewHolder(var binding: CustomUserViewBinding) : ViewHolder(binding.root)

    private var friendsList = ArrayList<User>()
    private lateinit var context: Context
    lateinit var onClickUser : ((User) ->Unit)
    fun setFriendsList(friendsList: ArrayList<User>, context: Context){
        this.friendsList = friendsList
        this.context = context
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return  FriendViewHolder(
            CustomUserViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        )
    }

    override fun getItemCount(): Int = friendsList.size

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val user = friendsList[position]
        Glide.with(holder.itemView)
            .load(user.userImage)
            .into(holder.binding.userImageId)

        if (user.userImage != ""){
            Glide.with(holder.itemView)
                .load(user.userImage)
                .into(holder.binding.userImageId)
        }else{
            holder.binding.userImageId.setImageResource(R.drawable.profile)
        }

        holder.binding.userNameId.text = user.userName
        
        if (user.isOnline!!){
            holder.binding.btnIsOnLineId.setColorFilter(ContextCompat.getColor(context, R.color.green_color))
        }else{
            holder.binding.btnIsOnLineId.setColorFilter(ContextCompat.getColor(context, R.color.red_color))
        }


        holder.itemView.setOnClickListener {
            onClickUser.invoke(user)
        }

    }
}