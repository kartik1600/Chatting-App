package com.example.chattingapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chattingapp.MyViewModel
import com.example.chattingapp.UserData
import com.example.chattingapp.chatinbox.ChatInBoxPageArgs
import com.example.chattingapp.databinding.FragmentHomePageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database


class HomePage : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val adapter = HomePageAdapter()
    private val myViewModel by lazy { ViewModelProvider(this)[MyViewModel::class.java] }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.btnLogOut.setOnClickListener {
            auth.signOut()
            findNavController().navigate(HomePageDirections.actionHomePageToLoginPage())
            myViewModel.toast("Log out successfully", requireContext())
        }

        adapter.onClick = { userData ->
            myViewModel.toast(userData.name.toString(), requireContext())
            findNavController().navigate(HomePageDirections.actionHomePageToChatInBoxPage(userData))
        }


        adapter.itemList.clear()
        setAdapter()
        listenUserList()


    }

    private fun listenUserList() {

        val userIdReference = Firebase.database.reference
            .child("users")

        userIdReference.get().addOnSuccessListener { dataSnapShot ->
            val itemList = mutableListOf<UserData>()
            for (postSnapshot in dataSnapShot.children) {
                val data = postSnapshot.getValue(UserData::class.java)     //here type is defined
                data?.let {
                    itemList.add(it)
                }
            }
            adapter.itemList.addAll(itemList.filter { it.uid != auth.uid })
            adapter.notifyItemRangeChanged(0, adapter.itemCount)

        }.addOnFailureListener {
            Log.e("TAG", "setAdapter: ${it.message}")
        }
    }

    private fun setAdapter() {
        binding.recycleView.adapter = adapter
    }
}
