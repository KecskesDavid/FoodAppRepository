package com.example.foodproject.fragments.profile

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
import com.example.foodproject.data.User
import com.example.foodproject.util.Constants
import com.example.foodproject.util.Constants.Companion.emailSP
import com.example.foodproject.viewmodel.UserViewModel
import com.google.android.material.textfield.TextInputLayout


class ChangePasswordFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)
        val UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val sharedPreferences = context?.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)


        val change = view.findViewById<Button>(R.id.change_Btn)
        val nav = findNavController()

        val cur_password = view.findViewById<TextInputLayout>(R.id.editCurrentPassword)
        val new_password = view.findViewById<TextInputLayout>(R.id.editTextNewPassword)
        val re_new_password = view.findViewById<TextInputLayout>(R.id.editTextReEnterNewPassword)

        change.setOnClickListener {
            UserViewModel.readAllUsers.observe(viewLifecycleOwner, Observer { users ->

                users.forEach { user ->
                    if(user.email == sharedPreferences?.getString(emailSP,""))
                    {
                        if(checkInput(cur_password,new_password,re_new_password,user.password))
                        {
                            val new_user = User(user.id,user.name,user.email,user.address,user.job,user.phone,new_password.editText?.text.toString(),"")

                            UserViewModel.updateUser(new_user)

                            nav.navigate(R.id.action_changePasswordFragment_to_navigation_profile)
                        }
                    }
                }

            })

        }

        return view
    }

    fun checkInput( cur_password: TextInputLayout, new_password: TextInputLayout,  re_new_password: TextInputLayout, cur_pass_string: String): Boolean
    {
        var bool: Boolean = true

        if(new_password.editText?.text?.isEmpty() == true)
        {
            new_password.setError("Empty field!")
            bool = false
        }
        if(re_new_password.editText?.text?.isEmpty() == true)
        {
            re_new_password.setError("Empty field!")
            bool = false
        }
        if(cur_password.editText?.text?.isEmpty() == true)
        {
            cur_password.setError("Empty field!")
            bool = false
        }

        //passwords doesn't match
        if( ! new_password.editText?.text.toString().equals(re_new_password.editText?.text.toString()) )
        {
            re_new_password.setError("The passwords do not match!")
            bool=false
        }

        if( new_password.editText?.text.toString().equals(cur_password.editText?.text.toString()) )
        {
            new_password.setError("Current and New cannot match!")
            bool=false
        }


        if( ! cur_password.editText?.text.toString().equals(cur_pass_string) )
        {
            cur_password.setError("Wrong password!")
            bool=false
        }

        when(bool)
        {
            true -> return true
            false -> return false
        }
    }


}