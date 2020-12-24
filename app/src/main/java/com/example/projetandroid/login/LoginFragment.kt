package com.example.projetandroid.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.example.projetandroid.MainActivity
import com.example.projetandroid.R
import com.example.projetandroid.SHARED_PREF_TOKEN_KEY
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

        val testLog = PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREF_TOKEN_KEY, "")

        if(testLog != "") {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        val loginButton = view.findViewById<Button>(R.id.log_in_button)
        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)

        email.addTextChangedListener {
            loginButton.isEnabled = !((email.text.toString() == "" || password.text.toString() == "") && password.text.length >= 6)
        }
        password.addTextChangedListener {
            loginButton.isEnabled = !((email.text.toString() == "" || password.text.toString() == "") && password.text.length >= 6)
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
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString(SHARED_PREF_TOKEN_KEY, response.token)
                    }
                    val t = PreferenceManager.getDefaultSharedPreferences(context).getString(SHARED_PREF_TOKEN_KEY, "")
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}