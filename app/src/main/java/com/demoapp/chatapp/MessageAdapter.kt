package com.demoapp.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, var messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val ITEM_RECIEVE = 1
        val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == 1){
            // inflate receiver
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }
        else{
            // inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {

        val CurrentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(CurrentMessage.senderId)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECIEVE
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val Currentmessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val viewholder  = holder as SentViewHolder
            holder.sent.text = Currentmessage.message
        }
        else{
            val viewholder = holder as ReceiveViewHolder
            holder.receive.text = Currentmessage.message
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sent = itemView.findViewById<TextView>(R.id.sentTxt)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receive = itemView.findViewById<TextView>(R.id.ReceiveTxt)
    }
}