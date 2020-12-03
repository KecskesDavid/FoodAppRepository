package com.example.foodproject.fragments.profile

import android.content.Context
import android.content.SharedPreferences
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
import com.example.foodproject.util.Constants
import com.example.foodproject.util.Constants.Companion.addressSP
import com.example.foodproject.util.Constants.Companion.emailSP
import com.example.foodproject.util.Constants.Companion.idSP
import com.example.foodproject.util.Constants.Companion.nameSP
import com.example.foodproject.util.Constants.Companion.phoneSP
import com.example.foodproject.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val sharedPreferences = context?.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        val login_btn = view.findViewById<Button>(R.id.to_Login_Btn)
        val register_btn = view.findViewById<Button>(R.id.to_Register_Btn)
        val logout_btn = view.findViewById<Button>(R.id.logout_Btn)
        val nav = findNavController()

        val isActiveUser = sharedPreferences?.getString(nameSP,"")

        if( isActiveUser != "" )
        {
            login_btn.visibility=View.GONE
            register_btn.visibility=View.GONE
            logout_btn.visibility=View.VISIBLE

            val name = view.findViewById<TextView>(R.id.nameTxt)
            val address = view.findViewById<TextView>(R.id.addressTxt)
            val phone = view.findViewById<TextView>(R.id.phoneTxt)
            val email = view.findViewById<TextView>(R.id.emailTxt)

            name.text = sharedPreferences?.getString(nameSP,"")
            address.text = sharedPreferences?.getString(addressSP,"")
            phone.text = sharedPreferences?.getString(phoneSP,"")
            email.text = sharedPreferences?.getString(emailSP,"")
        }

        login_btn.setOnClickListener {
            nav.navigate(R.id.action_navigation_profile_to_loginFragment)
        }

        register_btn.setOnClickListener {
            nav.navigate(R.id.action_navigation_profile_to_registerFragment)
        }

        logout_btn.setOnClickListener {
            with (sharedPreferences!!.edit()) {
                remove(nameSP)
                remove(addressSP)
                remove(emailSP)
                remove(phoneSP)
                remove(idSP)
                nav.navigate(R.id.navigation_profile)
                apply()
            }
        }

        return view
    }

}