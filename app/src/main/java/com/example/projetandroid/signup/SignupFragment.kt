package com.example.projetandroid.signup

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
import com.example.projetandroid.login.LoginForm
import com.example.projetandroid.userinfo.UserInfoViewModel
import kotlinx.coroutines.launch

class SignupFragment: Fragment()  {

    private val userViewModel : UserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loginButton = view.findViewById<Button>(R.id.sign_up_button)
        val firstname = view.findViewById<EditText>(R.id.first_name)
        val lastname = view.findViewById<EditText>(R.id.last_name)
        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)
        val confirmPassword = view.findViewById<EditText>(R.id.confirm_password)

        firstname.addTextChangedListener {
            loginButton.isEnabled = !(
                    (firstname.text.toString() == "" || lastname.text.toString() == "" || email.text.toString() == "" || password.text.toString() == ""|| confirmPassword.text.toString() == "")
                            || confirmPassword.text.toString() != password.text.toString()
                    )
        }
        lastname.addTextChangedListener {
            loginButton.isEnabled = !(
                    (firstname.text.toString() == "" || lastname.text.toString() == "" || email.text.toString() == "" || password.text.toString() == ""|| confirmPassword.text.toString() == "")
                            || confirmPassword.text.toString() != password.text.toString()
                    )
        }
        email.addTextChangedListener {
            loginButton.isEnabled = !(
                    (firstname.text.toString() == "" || lastname.text.toString() == "" || email.text.toString() == "" || password.text.toString() == ""|| confirmPassword.text.toString() == "")
                            || confirmPassword.text.toString() != password.text.toString()
                    )
        }
        password.addTextChangedListener {
            loginButton.isEnabled = !(
                    (firstname.text.toString() == "" || lastname.text.toString() == "" || email.text.toString() == "" || password.text.toString() == ""|| confirmPassword.text.toString() == "")
                            || confirmPassword.text.toString() != password.text.toString()
                    )
        }
        confirmPassword.addTextChangedListener {
            loginButton.isEnabled = !(
                    (firstname.text.toString() == "" || lastname.text.toString() == "" || email.text.toString() == "" || password.text.toString() == ""|| confirmPassword.text.toString() == "")
                            || confirmPassword.text.toString() != password.text.toString()
                    )
        }

        loginButton.isClickable = false
        loginButton.isEnabled = false

        loginButton.setOnClickListener{
            val user =  SignupForm(firstname?.text.toString(),lastname?.text.toString(),email?.text.toString(),password?.text.toString(),confirmPassword?.text.toString())
            lifecycleScope.launch {
                val response = userViewModel.signup(user).body()
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