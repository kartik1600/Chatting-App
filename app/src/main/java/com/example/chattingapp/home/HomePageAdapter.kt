package com.example.chattingapp.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.chattingapp.UserData
import com.example.chattingapp.databinding.UserBoxBinding

class HomePageAdapter : RecyclerView.Adapter<HomePageAdapter.ViewHolder>() {

    val itemList = ArrayList<UserData>()

    inner class ViewHolder(private val binding: UserBoxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(position: Int) {
            binding.tvUsername.text = itemList[position].name

            binding.root.setOnClickListener {
                onClick?.invoke(itemList[position])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UserBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(position)
    }

    var onClick: ((UserData) -> Unit)? = null

}