package com.example.foodproject.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.foodproject.R
import com.example.foodproject.data.User
import com.example.foodproject.util.Constants
import com.example.foodproject.util.Constants.Companion.PREFERENCES
import com.example.foodproject.util.Constants.Companion.addressSP
import com.example.foodproject.util.Constants.Companion.emailSP
import com.example.foodproject.util.Constants.Companion.nameSP
import com.example.foodproject.util.Constants.Companion.phoneSP
import com.example.foodproject.viewmodel.UserViewModel


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
        val name = view.findViewById<EditText>(R.id.editTextPersonName)
        val email = view.findViewById<EditText>(R.id.editTextEmail)
        val address = view.findViewById<EditText>(R.id.editTextAddress)
        val pass1 = view.findViewById<EditText>(R.id.editTextPassword)
        val pass2 = view.findViewById<EditText>(R.id.editTextPassword2)
        val phone = view.findViewById<EditText>(R.id.editTextPhone)

        register_Btn.setOnClickListener{

            if( checkInput(name,email,address,pass1,pass2,phone) ){

                UserViewModel.readAllUsers.observe(viewLifecycleOwner, Observer { user ->

                    var bool:Boolean = true
                    user.forEach {
                        if(it.email.equals(email.text.toString()))
                        {
                            email.setError("This email address is already taken!")
                            bool = false
                        }
                    }

                    if(bool)
                    {
                        Toast.makeText(context,"User added",Toast.LENGTH_SHORT).show()

                        UserViewModel.addUser(User(0,name.text.toString(),email.text.toString(),address.text.toString(),phone.text.toString(),pass1.text.toString(),""))
                        //todo try without foreach -> new query
                        UserViewModel.readAllUsers.observe(viewLifecycleOwner, Observer { restaurant ->

                            restaurant.forEach {
                                if(it.email.equals(email.text.toString())){

                                    with (sharedPreferences!!.edit()) {
                                        putString(nameSP,name.text.toString())
                                        putString(addressSP,address.text.toString())
                                        putString(emailSP,email.text.toString())
                                        putString(phoneSP,phone.text.toString())
                                        putInt(Constants.idSP,it.id)
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

    fun checkInput( name: EditText,  email: EditText,  address: EditText,  pass1: EditText,  pass2: EditText,  phone: EditText): Boolean
    {
        var bool: Boolean = true
        //if any of this are empty then set an error message
        if(name.text.isEmpty())
        {
            name.setError("Empty field!")
            bool = false
        }
        if(email.text.isEmpty())
        {
            email.setError("Empty field!")
            bool = false
        }
        if(address.text.isEmpty())
        {
            address.setError("Empty field!")
            bool = false
        }
        if(pass1.text.isEmpty())
        {
            pass1.setError("Empty field!")
            bool = false
        }
        if(pass2.text.isEmpty())
        {
            pass2.setError("Empty field!")
            bool = false
        }
        if(phone.text.isEmpty())
        {
            phone.setError("Empty field!")
            bool = false
        }

        //passwords doesn't match
        if( ! pass1.text.toString().equals(pass2.text.toString()) )
        {
            pass2.setError("The passwords do not match!")
            bool=false
        }

        if( phone.text.length != 10 || phone.text[0] != '0')
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