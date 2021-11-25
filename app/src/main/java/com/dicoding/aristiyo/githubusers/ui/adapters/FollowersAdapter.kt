package com.dicoding.aristiyo.githubusers.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aristiyo.githubusers.databinding.FollowerAdapterItemBinding
import com.dicoding.aristiyo.githubusers.models.UserFollowersListItem

class FollowersAdapter(private val listUsers: ArrayList<UserFollowersListItem>) :
    RecyclerView.Adapter<FollowersAdapter.FollowerAdapterHolders>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(userFollowersList: List<UserFollowersListItem>) {
        listUsers.apply {
            clear()
            addAll(userFollowersList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerAdapterHolders {
        return FollowerAdapterHolders(
            FollowerAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FollowerAdapterHolders, position: Int) {
        val data = listUsers[position]
        holder.binding.apply {
            Glide.with(root).load(data.avatarUrl).into(imgPhoto)
            txtUsername.text = data.login
            txtTypeStatus.text = data.type
        }
    }

    override fun getItemCount(): Int = listUsers.size

    class FollowerAdapterHolders(var binding: FollowerAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}