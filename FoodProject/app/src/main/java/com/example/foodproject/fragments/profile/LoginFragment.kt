package com.example.foodproject.fragments.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodproject.R
import com.example.foodproject.util.Constants.Companion.PREFERENCES
import com.example.foodproject.util.Constants.Companion.addressSP
import com.example.foodproject.util.Constants.Companion.emailSP
import com.example.foodproject.util.Constants.Companion.idSP
import com.example.foodproject.util.Constants.Companion.jobSP
import com.example.foodproject.util.Constants.Companion.nameSP
import com.example.foodproject.util.Constants.Companion.passSP
import com.example.foodproject.util.Constants.Companion.phoneSP
import com.example.foodproject.util.Constants.Companion.photoSP
import com.example.foodproject.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.runBlocking

class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val sharedPreferences = context?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val login_Btn = view.findViewById<Button>(R.id.login_Btn)
        val email = view.findViewById<TextInputLayout>(R.id.email_Text_View)
        val pass = view.findViewById<TextInputLayout>(R.id.pass_Text_view)

        login_Btn.setOnClickListener {

            val user = runBlocking{ UserViewModel.readUserByEmail(email.editText?.text.toString()) }

            user.observe(viewLifecycleOwner, Observer {
                if(it != null ) {
                    if (!it.email.isEmpty()) {

                        if (pass.editText?.text.toString().equals(it.password)) {
                            with(sharedPreferences!!.edit()) {
                                putString(nameSP, it.name)
                                putString(addressSP, it.address)
                                putString(emailSP, it.email)
                                putString(phoneSP, it.phone)
                                putString(jobSP, it.job)
                                putString(passSP, it.password)
                                putString(photoSP, it.photo)
                                putInt(idSP, it.id)
                                apply()
                            }

                            val nav = findNavController()
                            nav.navigate(R.id.action_loginFragment_to_navigation_profile)
                        }
                        else{
                            pass.setError("Wrong password!")
                        }

                    } else {
                        email.setError("Empty field!")
                    }

                }
                else{
                    email.setError("Wrong email address!")
                }

            })

        }

        return view
    }

}