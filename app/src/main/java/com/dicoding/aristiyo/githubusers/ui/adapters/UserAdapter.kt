package com.dicoding.aristiyo.githubusers.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aristiyo.githubusers.databinding.UserAdapterItemBinding
import com.dicoding.aristiyo.githubusers.models.SearchUsersResponseItem

class UserAdapter(private val listUsers: ArrayList<SearchUsersResponseItem>) :
    RecyclerView.Adapter<UserAdapter.AdapterHolders>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClicked(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: List<SearchUsersResponseItem>) {
        listUsers.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolders {
        return AdapterHolders(
            UserAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AdapterHolders, position: Int) {
        val data = listUsers[position]
        holder.binding.apply {
            Glide.with(root)
                .load(data.avatarUrl)
                .fitCenter()
                .into(imgPhoto)
            txtUsername.text = data.login
            txtTypeStatus.text = data.type
            root.setOnClickListener {
                onItemClickCallback.onItemClicked(
                    listUsers[holder.adapterPosition]
                )
            }
        }
    }

    override fun getItemCount(): Int = listUsers.size

    class AdapterHolders(var binding: UserAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: SearchUsersResponseItem)
    }
}