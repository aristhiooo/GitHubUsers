package com.dicoding.aristiyo.githubusers.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aristiyo.githubusers.R
import com.dicoding.aristiyo.githubusers.databinding.ActivityMainBinding
import com.dicoding.aristiyo.githubusers.models.SearchUsersResponseItem
import com.dicoding.aristiyo.githubusers.ui.adapters.UserAdapter
import com.dicoding.aristiyo.githubusers.ui.detailuser.DetailUserActivity
import com.dicoding.aristiyo.githubusers.viewmodels.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var mView: ActivityMainBinding
    private val mainViewModel by viewModels<ViewModelsWithKTX>()
    private lateinit var settingViewModel: ViewModelsWithContext

    private var switchTheme: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mView.root)
        setSupportActionBar(mView.toolbarMain)
        supportActionBar?.title = ""

        settingViewModel = ViewModelProvider(this,
                ViewModelsFactory.getInstance(this.application)
            )[ViewModelsWithContext::class.java]
        settingViewModel.getThemeSettings().observe(this, { isDarkThemeActive: Boolean ->
            switchTheme = if (isDarkThemeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                1
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                0
            }

        })

        mainViewModel.dataSearchUsersResponse.observe(this, { dataSet ->
            mView.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = UserAdapter(arrayListOf()).apply {
                    setOnItemClicked(object : UserAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: SearchUsersResponseItem) {
                            Toast.makeText(
                                applicationContext,
                                "${data.login} dipilih",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    DetailUserActivity::class.java
                                ).putExtra(DetailUserActivity.KEY_USERNAME, data.login)
                            )
                        }
                    })
                }.also { it.setData(dataSet.items) }
            }
        })

        mView.txtInputUsernameHint.editText?.apply {
            doOnTextChanged { text, _, _, _ ->
                if (!text.isNullOrEmpty()) {
                    mainViewModel.doGetSearchUser(text.toString())
                    mView.welcomeView.visibility = View.GONE
                } else {
                    mView.welcomeView.visibility = View.VISIBLE
                    mView.loadingProgress.visibility = View.GONE
                    mView.recyclerView.visibility = View.GONE
                }
            }
        }

        mainViewModel.isLoading.observe(this, {
            onLoading(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        FavoriteActivity::class.java
                    )
                )
            }
            R.id.menu_settings -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Pengaturan Tema")
                    .setSingleChoiceItems(arrayOf("Light", "Dark"), switchTheme!!) { _, which ->
                        when (which) {
                            0 -> {
                                settingViewModel.saveThemeSetting(false)
                            }
                            1 -> {
                                settingViewModel.saveThemeSetting(true)
                            }
                        }
                    }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun onLoading(isLoading: Boolean) {
        if (isLoading) {
            mView.loadingProgress.visibility = View.VISIBLE
            mView.recyclerView.visibility = View.GONE
        } else if (!isLoading) {
            mView.loadingProgress.visibility = View.GONE
            mView.recyclerView.visibility = View.VISIBLE
        }
    }
}