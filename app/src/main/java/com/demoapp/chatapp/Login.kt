package com.demoapp.chatapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class Login : AppCompatActivity() {

    private lateinit var loginText: TextView
    private lateinit var emailedit: EditText
    private lateinit var passwordedit: EditText
    private lateinit var loginbtn: Button
    private lateinit var signupbtn: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        loginText = findViewById(R.id.loginText);
        emailedit = findViewById(R.id.emailedit)
        passwordedit = findViewById(R.id.passwordedit)
        loginbtn = findViewById(R.id.loginbtn)
        signupbtn = findViewById(R.id.signupbtn)

        signupbtn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        mAuth = FirebaseAuth.getInstance()

        loginbtn.setOnClickListener {
            val email = emailedit.text.toString()
            val pass = passwordedit.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                // Display a message to the user indicating that both email and password are required
                Toast.makeText(
                    this@Login,
                    "Please enter both email and password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                login(email, pass)
            }
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this@Login, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        // Handle case when user account is not found
                        Toast.makeText(this@Login, "User not found", Toast.LENGTH_SHORT).show()
                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        // Handle case when invalid credentials are provided
                        Toast.makeText(this@Login, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle other authentication failures
                        Toast.makeText(this@Login, "Authentication failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }
}
