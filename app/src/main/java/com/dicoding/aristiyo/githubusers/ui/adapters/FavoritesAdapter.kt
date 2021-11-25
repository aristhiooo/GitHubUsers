package com.dicoding.aristiyo.githubusers.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aristiyo.githubusers.databinding.FavoriteAdapterItemBinding
import com.dicoding.aristiyo.githubusers.models.UserDetails
import com.dicoding.aristiyo.githubusers.ui.adapters.FavoritesAdapter.AdapterHolders

class FavoritesAdapter(private val listFavoriteUsers: ArrayList<UserDetails>) :
    RecyclerView.Adapter<AdapterHolders>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClicked(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataSet: List<UserDetails>?) {
        listFavoriteUsers.apply {
            clear()
            if (dataSet != null) {
                addAll(dataSet)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolders {
        return AdapterHolders(
            FavoriteAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AdapterHolders, position: Int) {
        val data = listFavoriteUsers[position]
        holder.binding.apply {
            Glide.with(root)
                .load(data.avatarUrl)
                .fitCenter()
                .into(imgPhoto)
            txtFullName.text = data.name
            txtUsername.text = data.login
            txtBio.text = (if (data.bio.isNullOrEmpty()) {
                "Belum ada bio yang diisi oleh pengguna ini"
            } else data.bio)
            root.setOnClickListener {
                onItemClickCallback.onItemClicked(
                    listFavoriteUsers[holder.adapterPosition]
                )
            }
        }
    }

    override fun getItemCount(): Int = listFavoriteUsers.size

    class AdapterHolders(var binding: FavoriteAdapterItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: UserDetails)
    }
}