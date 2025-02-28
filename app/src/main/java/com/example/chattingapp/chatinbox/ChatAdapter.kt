package com.example.chattingapp.chatinbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.chattingapp.Message
import com.example.chattingapp.R
import com.example.chattingapp.databinding.LeftChatBinding
import com.example.chattingapp.databinding.RightChatBinding

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    val itemList = ArrayList<Message>()
    var myUID: String = ""

    private val senderSide = 1
    private val receiverSide = 2

    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(position: Int, itemViewType: Int) {
            when (itemViewType) {
                senderSide -> {
                    (binding as RightChatBinding).apply {
                        ivUserProfile.setImageResource(R.drawable.account_circle_icon)
                        tvChat.text = itemList[position].message
                    }
                }
                receiverSide -> {
                    (binding as LeftChatBinding).apply {
                        ivUserProfile.setImageResource(R.drawable.account_circle_icon)
                        tvChat.text = itemList[position].message
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            if (senderSide == viewType) RightChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ) else LeftChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(position, holder.itemViewType)



    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].currentUid == myUID) senderSide else receiverSide
    }
}