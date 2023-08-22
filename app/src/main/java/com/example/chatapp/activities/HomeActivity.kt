package com.example.chatapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityHomeBinding


import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.DatabaseReference


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dataRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navBottomId = binding.bottomNavigationId
        val navController = Navigation.findNavController(this, R.id.frameLayoutId)
        navBottomId.setupWithNavController(navController)




        /*auth = FirebaseAuth.getInstance()

        val currentUser = Firebase.auth.currentUser





        dataRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser!!.uid)

        dataRef.get().addOnSuccessListener {
            val user = it.getValue(User::class.java)!!
            binding.userNameTxt.text = user.userName
            Log.i("myMehenni", "Got value ${user.userId}")

        }.addOnFailureListener {
            Log.e("myMehenni", "Error getting data", it)

        }*/




    }
}