package com.demoapp.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerview: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        recyclerview = findViewById(R.id.UserRecyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        mDbRef.child("user").addValueEventListener(object : ValueEventListener{

            //SNAPSHOT IS USED TO GET DATA FROM DATABASE
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                    for(postSnapshot in snapshot.children){
                        val currenUser = postSnapshot.getValue(User::class.java)
                        if(mAuth.currentUser?.uid != currenUser?.uid) {
                            userList.add(currenUser!!)
                        }
                    }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            mAuth.signOut()
            finish()
            return true
        }
        return true
    }
}