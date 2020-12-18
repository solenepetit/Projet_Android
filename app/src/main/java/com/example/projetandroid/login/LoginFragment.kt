package com.example.projetandroid.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projetandroid.MainActivity
import com.example.projetandroid.R
import com.example.projetandroid.network.Api
import com.example.projetandroid.tasklist.TaskListFragment
import com.example.projetandroid.tasklist.task.TaskActivity
import com.example.projetandroid.userinfo.UserInfoViewModel
import kotlinx.coroutines.launch

class LoginFragment: Fragment()  {

    private val userViewModel : UserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loginButton = view.findViewById<Button>(R.id.log_in_button)
        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)

        email.addTextChangedListener {
            loginButton.isEnabled = !(email.text.toString() == "" || password.text.toString() == "")
        }
        password.addTextChangedListener {
            loginButton.isEnabled = !(email.text.toString() == "" || password.text.toString() == "")
        }
        loginButton.isClickable = false
        loginButton.isEnabled = false

        loginButton.setOnClickListener{
            val user =  LoginForm(email?.text.toString(),password?.text.toString())
            lifecycleScope.launch {
                val response = userViewModel.login(user).body()
                if(response?.token == null) {
                    Toast.makeText(context, response?.token.toString(), Toast.LENGTH_LONG).show()
                }
                else{
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}