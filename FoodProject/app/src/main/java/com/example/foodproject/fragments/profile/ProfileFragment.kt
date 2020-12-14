package com.example.foodproject.fragments.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodproject.R
import com.example.foodproject.model.User
import com.example.foodproject.util.Constants
import com.example.foodproject.util.Constants.Companion.addressSP
import com.example.foodproject.util.Constants.Companion.emailSP
import com.example.foodproject.util.Constants.Companion.idSP
import com.example.foodproject.util.Constants.Companion.jobSP
import com.example.foodproject.util.Constants.Companion.nameSP
import com.example.foodproject.util.Constants.Companion.phoneSP
import com.example.foodproject.util.Constants.Companion.photoSP
import com.example.foodproject.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    var imageUri: String = ""
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val sharedPreferences = context?.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        val UserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val profile_image = view.findViewById<ImageView>(R.id.profile_image)
        val login_btn = view.findViewById<Button>(R.id.to_Login_Btn)
        val register_btn = view.findViewById<Button>(R.id.to_Register_Btn)
        val logout_btn = view.findViewById<Button>(R.id.logout_Btn)
        val change_pass_btn = view.findViewById<ImageView>(R.id.change_Password_Btn)
        val nav = findNavController()

        val isActiveUser = sharedPreferences?.getString(nameSP, "")

        if (isActiveUser != "") {
            login_btn.visibility = View.GONE
            register_btn.visibility = View.GONE
            logout_btn.visibility = View.VISIBLE
            change_pass_btn.visibility = View.VISIBLE

            val name = view.findViewById<TextView>(R.id.nameTxt)
            val address = view.findViewById<TextView>(R.id.addressTxt)
            val phone = view.findViewById<TextView>(R.id.phoneTxt)
            val email = view.findViewById<TextView>(R.id.emailTxt)
            val job = view.findViewById<TextView>(R.id.jobTxt)

            if (sharedPreferences?.getString(photoSP, "") != "") {
                Glide.with(requireContext())
                        .load(sharedPreferences?.getString(photoSP, ""))
                        .into(profile_image)
            }
            name.text = sharedPreferences?.getString(nameSP, "")
            address.text = sharedPreferences?.getString(addressSP, "")
            phone.text = sharedPreferences?.getString(phoneSP, "")
            email.text = sharedPreferences?.getString(emailSP, "")
            job.text = sharedPreferences?.getString(jobSP, "")

            profile_image.setOnClickListener {
                //function to change the profile picture
                selectImageFromGallery()
            }
        }

        login_btn.setOnClickListener {
            nav.navigate(R.id.action_navigation_profile_to_loginFragment)
        }

        register_btn.setOnClickListener {
            nav.navigate(R.id.action_navigation_profile_to_registerFragment)
        }

        change_pass_btn.setOnClickListener {
            nav.navigate(R.id.action_navigation_profile_to_changePasswordFragment)
        }

        logout_btn.setOnClickListener {
            //if the photo is updated then i update the user then logout
            if (imageUri != "") {
                UserViewModel.readAllUsers.observe(viewLifecycleOwner, Observer { users ->

                    users.forEach { user ->

                        if (user.email == sharedPreferences?.getString(emailSP, "")) {
                            val new_user = User(user.id, user.name, user.email, user.address, user.job, user.phone, user.password, imageUri)

                            UserViewModel.updateUser(new_user)

                            with(sharedPreferences.edit()) {
                                remove(nameSP)
                                remove(addressSP)
                                remove(emailSP)
                                remove(phoneSP)
                                remove(jobSP)
                                remove(idSP)
                                nav.navigate(R.id.navigation_profile)
                                apply()
                            }

                        }

                    }
                })
            } else {
                with(sharedPreferences!!.edit()) {
                    remove(nameSP)
                    remove(addressSP)
                    remove(emailSP)
                    remove(phoneSP)
                    remove(jobSP)
                    remove(idSP)
                    nav.navigate(R.id.navigation_profile)
                    apply()
                }
            }

        }

        return view
    }

    private fun selectImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                pickImage()
            }
        } else {
            //system OS is > current
            pickImage()
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    //requesting permission from the user
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            Glide.with(requireContext())
                    .load(data?.data)
                    .into(profile_image)
            //saving profile pic uri in a variable, then saving it, if the user logs out
            imageUri = data?.data.toString()
            Toast.makeText(context, "To save the profile picture you have to logout first!", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val PICK_IMAGE = 1000
        private val PERMISSION_CODE = 1001
    }

}