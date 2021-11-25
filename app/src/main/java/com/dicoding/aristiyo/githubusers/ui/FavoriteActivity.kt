package com.dicoding.aristiyo.githubusers.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.aristiyo.githubusers.databinding.ActivityFavoriteBinding
import com.dicoding.aristiyo.githubusers.models.UserDetails
import com.dicoding.aristiyo.githubusers.ui.adapters.FavoritesAdapter
import com.dicoding.aristiyo.githubusers.ui.detailuser.DetailUserActivity
import com.dicoding.aristiyo.githubusers.viewmodels.ViewModelsWithContext
import com.dicoding.aristiyo.githubusers.viewmodels.ViewModelsFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModelsWithContext: ViewModelsWithContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.title = "Favorited Users"

        binding.toolbarMain.setNavigationOnClickListener { onBackPressed() }

        viewModelsWithContext = obtainViewModel(this@FavoriteActivity)
        viewModelsWithContext.getAllFavorites().observe(this, { dataSet ->
            if (dataSet.isEmpty()) {
                binding.rvFavorite.visibility = View.GONE
                binding.noDataView.visibility = View.VISIBLE
            } else {
                binding.rvFavorite.visibility = View.VISIBLE
                binding.noDataView.visibility = View.GONE
            }

            binding.rvFavorite.apply {
                layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
                adapter = FavoritesAdapter(arrayListOf()).apply {
                    setOnItemClicked(object : FavoritesAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: UserDetails) {
                            startActivity(
                                Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                                    .putExtra(DetailUserActivity.KEY_USERNAME, data.login)
                            )
                        }
                    })
                }.also { it.setData(dataSet) }
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): ViewModelsWithContext {
        val factory = ViewModelsFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ViewModelsWithContext::class.java]
    }
}