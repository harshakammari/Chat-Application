package com.demoapp.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    private lateinit var logo : ImageView
    private lateinit var emailedit : EditText
    private lateinit var passwordedit : EditText
    private lateinit var nameedit : EditText
    private lateinit var signupbtn : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbref : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        logo = findViewById(R.id.userimage)
        emailedit = findViewById(R.id.emailedit)
        passwordedit = findViewById(R.id.passwordedit)
        nameedit = findViewById(R.id.nameedit)
        signupbtn = findViewById(R.id.signupbtn)

        signupbtn.setOnClickListener {
            val name = nameedit.text.toString()
            val email = emailedit.text.toString()
            val pass = passwordedit.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                // Display a message to the user indicating that both email and password are required
                Toast.makeText(this@SignUp, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
            else {
                signUp(name, email, pass)
            }
        }
    }
        private fun signUp(name: String,email :String, password : String){
            // link to find the code https://firebase.google.com/docs/auth/android/password-auth
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        AddUsertoDataBase(name,email,mAuth.currentUser?.uid!!)
                        val intent = Intent(this@SignUp,MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@SignUp,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    private fun AddUsertoDataBase(name :String , email: String,uid:String){
        mDbref = FirebaseDatabase.getInstance().getReference()
        mDbref.child("user").child(uid).setValue(User(name,email,uid))

    }

}