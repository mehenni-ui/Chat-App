package com.example.chatapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.activities.ConversationActivity

import com.example.chatapp.adapters.FriendsAdapter
import com.example.chatapp.databinding.FragmentFeedBinding
import com.example.chatapp.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var dataRef: DatabaseReference
    private lateinit var friendsListDb : ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        friendsAdapter = FriendsAdapter()
        dataRef = FirebaseDatabase.getInstance().getReference("Users")
        friendsListDb = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getUsersFromDB()

        onUserClickListener()


        friendsAdapter.setFriendsList(friendsListDb, requireContext())
        binding.friendsRV.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = friendsAdapter
        }


    }

    private fun onUserClickListener() {
        friendsAdapter.onClickUser = { user ->
            val intent = Intent(context, ConversationActivity::class.java)

            intent.putExtra("userIdBundle", user.userId)
            intent.putExtra("userNameBundle", user.userName)
            intent.putExtra("userImageBundle", user.userImage)
            intent.putExtra("userIsOnLineBundle", user.isOnline!!)
            startActivity(intent)
        }
    }

    private fun getUsersFromDB() {
        dataRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                friendsListDb.clear()
                for (data in snapshot.children){
                    val user = data.getValue(User::class.java)!!
                    if (user.userId != Firebase.auth.currentUser!!.uid){
                        friendsListDb.add(user)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }


}