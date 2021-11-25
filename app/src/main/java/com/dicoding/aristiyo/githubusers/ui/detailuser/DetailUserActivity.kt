package com.dicoding.aristiyo.githubusers.ui.detailuser

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.aristiyo.githubusers.R
import com.dicoding.aristiyo.githubusers.databinding.ActivityDetailUserBinding
import com.dicoding.aristiyo.githubusers.viewmodels.ViewModelsWithContext
import com.dicoding.aristiyo.githubusers.viewmodels.ViewModelsFactory
import com.dicoding.aristiyo.githubusers.viewmodels.ViewModelsWithKTX
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var mView: ActivityDetailUserBinding
    private val viewModelsWithKTX by viewModels<ViewModelsWithKTX>()
    private lateinit var viewModelsWithContext: ViewModelsWithContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(mView.root)
        setSupportActionBar(mView.toolbarMain)
        supportActionBar?.title = "User Details"
        mView.toolbarMain.setNavigationOnClickListener { onBackPressed() }

        val paramUsername = intent.getStringExtra(KEY_USERNAME).toString()

        val paramToTabs = Bundle()
        paramToTabs.putString(KEY_USERNAME, paramUsername)

        viewModelsWithContext = ViewModelProvider(
            this,
            ViewModelsFactory.getInstance(this.application)
        )[ViewModelsWithContext::class.java]

        viewModelsWithKTX.doGetUserDetail(paramUsername)
        viewModelsWithKTX.dataDetailUserResponse.observe(this, { detailUsers ->
            Glide.with(this)
                .load(detailUsers.avatarUrl)
                .into(mView.profileImage)
            mView.txtName.text = detailUsers.name
            mView.txtUsername.text = detailUsers.login
            mView.txtBio.text = if (detailUsers.bio.isNullOrEmpty()) "Belum ada bio yang diisi oleh pengguna ini" else detailUsers.bio
            mView.txtRepoCount.text = detailUsers.publicRepos.toString()
            mView.txtFollowersCount.text = detailUsers.followers.toString()
            mView.txtFollowingCount.text = detailUsers.following.toString()

            var isFavorite = false
            CoroutineScope(Dispatchers.IO).launch {
                val count = viewModelsWithContext.check(paramUsername)
                withContext(Dispatchers.Main) {
                    isFavorite = count > 0
                    setBtnFavorite(isFavorite)
                }
            }

            mView.btnAddFavorite.setOnClickListener {
                isFavorite = !isFavorite
                if (isFavorite) {
                    viewModelsWithContext.insert(detailUsers)
                    Snackbar.make(mView.root, "Ditambahkan ke favorite", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    viewModelsWithContext.delete(detailUsers)
                    Snackbar.make(mView.root, "Dihapus dari favorite", Snackbar.LENGTH_SHORT).show()
                }
                setBtnFavorite(isFavorite)
            }
        })

        viewModelsWithKTX.isLoading.observe(this, {
            onLoading(it)
        })

        val viewPager: ViewPager2 = mView.viewPager
        viewPager.adapter = SectionsPagerAdapter(this, paramToTabs)
        val tabs: TabLayout = mView.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        const val KEY_USERNAME = "key_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.followings
        )
    }

    private fun setBtnFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            mView.btnAddFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            mView.btnAddFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }

    private fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            mView.loadingProgress.visibility = View.VISIBLE
            mView.group.visibility = View.GONE
        } else if (!isLoading) {
            mView.loadingProgress.visibility = View.GONE
            mView.group.visibility = View.VISIBLE
        }
    }
}

class SectionsPagerAdapter(activity: AppCompatActivity, bundle: Bundle) :
    FragmentStateAdapter(activity) {

    private var mBundle: Bundle = bundle

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersDetailUserFragment()
            1 -> fragment = FollowingDetailUserFragment()
        }
        fragment?.arguments = this.mBundle
        return fragment as Fragment
    }
}