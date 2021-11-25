package com.dicoding.aristiyo.githubusers.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aristiyo.githubusers.databinding.FollowingAdapterItemBinding
import com.dicoding.aristiyo.githubusers.models.UserFollowingsListItem

class FollowingsAdapter(private val listUsers: ArrayList<UserFollowingsListItem>) :
    RecyclerView.Adapter<FollowingsAdapter.FollowingAdapterHolders>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(userFollowingsList: List<UserFollowingsListItem>) {
        listUsers.apply {
            clear()
            addAll(userFollowingsList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingAdapterHolders {
        return FollowingAdapterHolders(
            FollowingAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FollowingAdapterHolders, position: Int) {
        val data = listUsers[position]
        holder.binding.apply {
            Glide.with(root)
                .load(data.avatarUrl)
                .into(imgPhoto)
            txtUsername.text = data.login
            txtTypeStatus.text = data.type
        }
    }

    override fun getItemCount(): Int = listUsers.size

    class FollowingAdapterHolders(var binding: FollowingAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}