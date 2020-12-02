package com.example.foodproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.foodproject.R

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        val login_Btn = view.findViewById<Button>(R.id.login_Btn)
        val nav = findNavController()

        login_Btn.setOnClickListener{
            nav.navigate(R.id.action_loginFragment_to_navigation_profile)
        }


        return view
    }

}