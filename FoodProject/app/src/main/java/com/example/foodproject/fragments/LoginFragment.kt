package com.example.foodproject.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodproject.R
import com.example.foodproject.util.Constants
import com.example.foodproject.util.Constants.Companion.PREFERENCES
import com.example.foodproject.viewmodel.UserViewModel

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        val sharedPreferences = context?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val login_Btn = view.findViewById<Button>(R.id.login_Btn)
        val email = view.findViewById<EditText>(R.id.email_Text_View)
        val pass = view.findViewById<EditText>(R.id.pass_Text_view)

        login_Btn.setOnClickListener{

            UserViewModel.readAllUsers.observe(viewLifecycleOwner, Observer { restaurant ->

                var boolPass = false
                restaurant.forEach { if( email.text.toString().equals(it.email) )
                {
                    if( pass.text.toString().equals(it.password) )
                    {
                        with (sharedPreferences!!.edit()) {
                            putString(Constants.nameSP,it.name)
                            putString(Constants.addressSP,it.address)
                            putString(Constants.emailSP,it.email)
                            putString(Constants.phoneSP,it.phone)
                            putString(Constants.jobSP,it.job)
                            putString(Constants.passSP,it.password)
                            putInt(Constants.idSP,it.id)
                            apply()
                        }

                        val nav = findNavController()
                        nav.navigate(R.id.action_loginFragment_to_navigation_profile)
                    }
                    boolPass = true
                }}

                if( boolPass )
                {
                    pass.setError("Wrong password!")
                }
                else
                {
                    email.setError("Wrong email address!")
                }
            })

        }


        return view
    }

}