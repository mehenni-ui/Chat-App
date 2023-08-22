package com.example.chatapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.chatapp.activities.HomeActivity
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignUpBinding
import com.example.chatapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var dataRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        dataRef = FirebaseDatabase.getInstance().getReference("Users")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtClickSignUpId.setOnClickListener {
            findNavController().navigate(R.id.loginFragmentId)
        }

        binding.btnSignUpId.setOnClickListener {
            registerUser()
        }




    }

    private fun registerUser() {

        val userNameTxt = binding.etUserNameSignUpId.text.toString()
        val emailTxt = binding.etEmailSignUpId.text.toString()
        val passwordTxt = binding.etPasswordSignUpId.text.toString()
        val confirmPasswordTxt = binding.etPasswordConfirmSignUpId.text.toString()

        if (TextUtils.isEmpty(userNameTxt) || TextUtils.isEmpty(emailTxt) ||TextUtils.isEmpty(passwordTxt) ||TextUtils.isEmpty(confirmPasswordTxt)){
            Toast.makeText(context, "Please, entre valid information", Toast.LENGTH_LONG).show()
        }else if (passwordTxt != confirmPasswordTxt){
            Toast.makeText(context, "password not does not much", Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnSuccessListener {
                Toast.makeText(context, "Sign up successfully", Toast.LENGTH_LONG).show()

                adduserToUsersLis(userNameTxt)
                startActivity(Intent(context, HomeActivity::class.java))


            }.addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun adduserToUsersLis(userNameTxt: String) {
        val userId = Firebase.auth.currentUser!!.uid
        val user = User(userId.toString(), userNameTxt, "", false)
        dataRef.child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(context, "user add to USERS successfully", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
    }
}