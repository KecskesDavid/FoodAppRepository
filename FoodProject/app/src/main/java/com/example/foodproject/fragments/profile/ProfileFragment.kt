package com.example.foodproject.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodproject.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val login_btn = view.findViewById<Button>(R.id.to_Login_Btn)
        val register_btn = view.findViewById<Button>(R.id.to_Register_Btn)
        val nav = findNavController()

        login_btn.setOnClickListener {
            nav.navigate(R.id.action_navigation_profile_to_loginFragment)
        }

        register_btn.setOnClickListener {
            nav.navigate(R.id.action_navigation_profile_to_registerFragment)
        }

        return view
    }
}