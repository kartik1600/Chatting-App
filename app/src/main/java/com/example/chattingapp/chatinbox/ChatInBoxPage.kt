package com.example.chattingapp.chatinbox

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.chattingapp.Message
import com.example.chattingapp.databinding.FragmentChatInBoxPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class ChatInBoxPage : Fragment() {

    private var _binding: FragmentChatInBoxPageBinding? = null
    private val binding get() = _binding!!
    private val adapter = ChatAdapter()
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val navArgs by navArgs<ChatInBoxPageArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatInBoxPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvUserName.text = navArgs.userData.name
        setAdapter()
        receiveMessage()

        binding.btnSendMessage.setOnClickListener {
            sendMessage()
            binding.etChatInput.text.clear()
        }
    }

    private fun receiveMessage() {
        val userIdReference = Firebase.database.reference.child("chat")

        userIdReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                adapter.itemList.clear()
                val itemList = mutableListOf<Message>()
                for (postSnapshot in snapshot.children) {
                    val data = postSnapshot.getValue(Message::class.java)
                    data?.let {
                        itemList.add(it)
                    }
                }
                adapter.itemList.addAll(itemList.filter { it.uniqueId == navArgs.userData.uid + auth.uid || it.uniqueId == auth.uid + navArgs.userData.uid })
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("", "receiveMessage: $error")
            }
        })
    }

    private fun sendMessage() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.run {
            val userIdReference = Firebase.database.reference.child("chat")
                .child(System.currentTimeMillis().toString())
            val message = Message(
                uniqueId = navArgs.userData.uid + auth.uid,
                message = binding.etChatInput.text.toString(),
                email = user.email,
                currentUid = user.uid
            )
            userIdReference.setValue(message)
        }
    }

    private fun setAdapter() {
        adapter.myUID = auth.currentUser?.uid.orEmpty()
        binding.recycleView.adapter = adapter

    }
}