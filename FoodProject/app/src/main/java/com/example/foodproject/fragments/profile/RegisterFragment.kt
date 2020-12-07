package com.example.foodproject.fragments.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodproject.R
import com.example.foodproject.data.User
import com.example.foodproject.util.Constants.Companion.PREFERENCES
import com.example.foodproject.util.Constants.Companion.addressSP
import com.example.foodproject.util.Constants.Companion.emailSP
import com.example.foodproject.util.Constants.Companion.idSP
import com.example.foodproject.util.Constants.Companion.jobSP
import com.example.foodproject.util.Constants.Companion.nameSP
import com.example.foodproject.util.Constants.Companion.passSP
import com.example.foodproject.util.Constants.Companion.phoneSP
import com.example.foodproject.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputLayout


class RegisterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val sharedPreferences = context?.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        val register_Btn = view.findViewById<Button>(R.id.register_Btn)
        val name = view.findViewById<TextInputLayout>(R.id.editTextPersonName)
        val email = view.findViewById<TextInputLayout>(R.id.editTextEmail)
        val address = view.findViewById<TextInputLayout>(R.id.editTextAddress)
        val job = view.findViewById<TextInputLayout>(R.id.editTextJob)
        val pass1 = view.findViewById<TextInputLayout>(R.id.editTextPassword)
        val pass2 = view.findViewById<TextInputLayout>(R.id.editTextPassword2)
        val phone = view.findViewById<TextInputLayout>(R.id.editTextPhone)

        register_Btn.setOnClickListener{

           if( checkInput(name,email,address,pass1,pass2,phone,job) ){

                UserViewModel.readAllUsers.observe(viewLifecycleOwner, Observer { user ->

                    var bool = true
                    user.forEach {
                        if(it.email.equals(email.editText?.text.toString()))
                        {
                            email.setError("This email address is already taken!")
                            bool = false
                        }
                    }

                    if(bool)
                    {
                        Toast.makeText(context,"User added",Toast.LENGTH_SHORT).show()

                        UserViewModel.addUser(User(0,name.editText?.text.toString(),email.editText?.text.toString(),address.editText?.text.toString(),job.editText?.text.toString(),phone.editText?.text.toString(),pass1.editText?.text.toString(),""))

                        UserViewModel.readAllUsers.observe(viewLifecycleOwner, Observer { restaurant ->

                            restaurant.forEach {
                                if(it.email.equals(email.editText?.text.toString())){

                                    with (sharedPreferences!!.edit()) {
                                        putString(nameSP,name.editText?.text.toString())
                                        putString(addressSP,address.editText?.text.toString())
                                        putString(emailSP,email.editText?.text.toString())
                                        putString(phoneSP,phone.editText?.text.toString())
                                        putString(jobSP,job.editText?.text.toString())
                                        putString(passSP,pass1.editText?.text.toString())
                                        putInt(idSP,it.id)
                                        apply()
                                    }

                                    //navigate back to profile loged in
                                    val nav = findNavController()
                                    nav.navigate(R.id.action_registerFragment_to_navigation_profile)
                                }
                            }

                        })

                    }
                })

            }
        }

        return view
    }

    fun checkInput( name: TextInputLayout,  email: TextInputLayout,  address: TextInputLayout,  pass1: TextInputLayout,  pass2: TextInputLayout,  phone: TextInputLayout, job: TextInputLayout): Boolean
    {
        var bool: Boolean = true
        //if any of this are empty then set an error message
        if(name.editText?.text?.isEmpty() == true)
        {
            name.setError("Empty field!")
            bool = false
        }
        if(email.editText?.text?.isEmpty() == true)
        {
            email.setError("Empty field!")
            bool = false
        }
        if(address.editText?.text?.isEmpty() == true)
        {
            address.setError("Empty field!")
            bool = false
        }
        if(job.editText?.text?.isEmpty() == true)
        {
            job.setError("Empty field!")
            bool = false
        }
        if(pass1.editText?.text?.isEmpty() == true)
        {
            pass1.setError("Empty field!")
            bool = false
        }
        if(pass2.editText?.text?.isEmpty() == true)
        {
            pass2.setError("Empty field!")
            bool = false
        }
        if(phone.editText?.text?.isEmpty() == true)
        {
            phone.setError("Empty field!")
            bool = false
        }

        //passwords doesn't match
        if( ! pass1.editText?.text.toString().equals(pass2.editText?.text.toString()) )
        {
            pass2.setError("The passwords do not match!")
            bool=false
        }

        if( phone.editText?.text?.length != 10 || phone.editText?.text?.get(0) != '0')
        {
            phone.setError("Phone number doesn't fit!")
            bool=false
        }

        when(bool)
        {
            true -> return true
            false -> return false
        }
    }

}