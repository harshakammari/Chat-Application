package com.demoapp.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.data.DataBufferSafeParcelable
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var messagerecyclerView : RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sentIcon : ImageView
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var mDbref : DatabaseReference

    var receiverRoom : String? =null
    var senderRoom : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        val name = intent.getStringExtra("name")
        val Receiveruid = intent.getStringExtra("uid")

        var senderUid = FirebaseAuth.getInstance().currentUser?.uid

        senderRoom = Receiveruid + senderUid
        receiverRoom = senderUid + Receiveruid




        supportActionBar?.title = name

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messagerecyclerView = findViewById(R.id.recyclerView)
        messageBox = findViewById(R.id.messageEditText)
        sentIcon = findViewById(R.id.sendButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)
        mDbref = FirebaseDatabase.getInstance().getReference()

        messagerecyclerView.layoutManager = LinearLayoutManager(this)
        messagerecyclerView.adapter = messageAdapter

        //logic for adding data to recycler view

        mDbref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object :ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        // adding message to database
        sentIcon.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)

            mDbref.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbref.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")
        }

    }

}