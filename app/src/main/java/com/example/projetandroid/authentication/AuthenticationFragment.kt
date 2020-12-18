package com.example.projetandroid.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.projetandroid.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AuthenticationFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loginButton = view.findViewById<MaterialButton>(R.id.log_in)
        loginButton.setOnClickListener{
            findNavController().navigate(R.id.action_fragment_authentication_to_fragment_login)
        }
        val signupButton = view.findViewById<MaterialButton>(R.id.sign_up)
        signupButton.setOnClickListener{
            findNavController().navigate(R.id.action_fragment_authentication_to_fragment_signup)
        }
    }
}