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
import com.example.chatapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private  var currentUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.txtClickSignUpId.setOnClickListener {
            findNavController().navigate(R.id.signUpFragmentId)
        }

        binding.btnLogInId.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val emailTxt = binding.etEmailLoginId.text.toString()
        val passwordTxt = binding.etPasswordLoginId.text.toString()

        if (TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(passwordTxt)){
            Toast.makeText(context, "please, enter a valid info", Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(emailTxt, passwordTxt)
                .addOnSuccessListener {
                    Toast.makeText(context, "Login successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(context, HomeActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Login faild", Toast.LENGTH_LONG).show()
                }
        }
    }


}