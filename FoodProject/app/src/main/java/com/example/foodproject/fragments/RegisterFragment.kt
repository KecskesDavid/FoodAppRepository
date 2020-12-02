package com.example.foodproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.foodproject.R


class RegisterFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val register_Btn = view.findViewById<Button>(R.id.register_Btn)
        val nav = findNavController()

        register_Btn.setOnClickListener{
            nav.navigate(R.id.action_registerFragment_to_navigation_profile)
        }


        return view
    }

}