package com.uche.safedealproject.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.firebase.auth.FirebaseUser
import com.uche.safedealproject.databinding.ActivitySignUpScreenBinding
import com.uche.safedealproject.extentions.Extensions.toast
import com.uche.safedealproject.utils.FirebaseUtils.firebaseAuth
import com.uche.safedealproject.utils.FirebaseUtils.firebaseUser

class SignUpScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpScreenBinding
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private lateinit var signUpScreenInputsArray: Array<EditText>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


       val emailTextEdit = binding.emailTextEdit
       val passwordTextEdit = binding.passwordTextEdit
        val  confirmPasswordEdit = binding.confirmPasswordEdit

       signUpScreenInputsArray = arrayOf(emailTextEdit, passwordTextEdit, confirmPasswordEdit)

        binding.signUpButton.setOnClickListener {
            signIn()
        }

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
            toast("Login into your account")
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            startActivity(Intent(this, HomePage::class.java))
            toast("Welcome back")
        }
    }

    private fun notEmpty(): Boolean = binding.emailTextEdit.text.toString().trim().isNotEmpty() &&
            binding.passwordTextEdit.text.toString().trim()
                .isNotEmpty() && binding.confirmPasswordEdit.text.toString().trim().isNotEmpty()

    private fun identicalPassword(): Boolean {
        var identical = false
        if (notEmpty() && binding.passwordTextEdit.text.toString()
                .trim() == binding.confirmPasswordEdit.text.toString().trim()
        ) {
            identical = true
        } else if (!notEmpty()) {
            signUpScreenInputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "This field is required"
                }
            }
        } else {
            toast("Passwords do not match!")
        }
        return identical
    }


    private fun signIn() {
        if (identicalPassword()) {
            // identicalPassword() returns true only when inputs are not empty and passwords are identical
           userEmail = binding.emailTextEdit.text.toString().trim()
            userPassword = binding.passwordTextEdit.text.toString().trim()

            /*create a user*/

            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener (this) { task ->
                    if (task.isSuccessful) {
                        toast("created account successfully!")
                            sendEmailVerification()
                        startActivity(Intent(this, LoginScreen::class.java))
                        finish()
                    } else {
                        toast("Your account creation failed. Please try again with correct details!")
                    }
                }
        }
    }

    /* this sends verification email to the new user. this only happens for non-null firebase user.
    */

    private fun sendEmailVerification() {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toast("email sent to $userEmail")
                }
            }
        }
    }

}