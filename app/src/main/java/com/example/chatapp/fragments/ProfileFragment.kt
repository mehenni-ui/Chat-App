package com.example.chatapp.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide


import com.example.chatapp.databinding.FragmentProfileBinding
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var launcher: ActivityResultLauncher<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var dataRefX: DatabaseReference
    private lateinit var dataRefT: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var USER_NAME: String
    private lateinit var USER_IMAGE: String
    var USER_ISONLINE: Boolean = false
    private lateinit var USER_ID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dataRefX = FirebaseDatabase.getInstance().getReference("Users")
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { file ->
            if (file != null) {
                binding.userImageProfileId.setImageURI(file)

                val reference = storage.reference.child("image").child(Date().time.toString())

                reference.putFile(file)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            reference.downloadUrl.addOnSuccessListener { task ->
                                uploadInfo(task.toString())
                            }
                        }
                    }
            } else {
                // Handle the case where 'file' is null
                Toast.makeText(context, "File is null", Toast.LENGTH_LONG).show()
            }
        }

        binding.userImageProfileId.setOnClickListener {
            launcher.launch("image/*")
        }







        dataRefT = FirebaseDatabase.getInstance().getReference("Users").child(
            Firebase.auth.currentUser!!.uid
        )

        dataRefT.get().addOnSuccessListener {
            val user = it.getValue(User::class.java)!!
            binding.userNameProfileId.text = user.userName
            binding.userIdProfileId.text = user.userId
            Glide.with(this).load(user.userImage).into(binding.userImageProfileId)

            USER_IMAGE = user.userImage.toString()
            USER_ISONLINE = user.isOnline!!
            USER_ID = user.userId!!
            USER_NAME = user.userName.toString()
            Log.i("myMehenni", "Got value id :  ${USER_ID}")

        }.addOnFailureListener {
            Log.e("myMehenni", "Error getting data", it)

        }
    }

    private fun uploadInfo(imageUrl: String) {
        val user = User(
            USER_ID,
            USER_NAME,
            imageUrl,
            true
        )

        dataRefX.child(USER_ID).setValue(user).addOnSuccessListener {
                Log.d("showUser", user.toString())
                Log.d("userImgUrlMehenni", user.userImage!!)
                Log.d("imgUrl", imageUrl)

                Toast.makeText(context, "IMAGE UPLOADED", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { err ->
                Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
            }
    }
}