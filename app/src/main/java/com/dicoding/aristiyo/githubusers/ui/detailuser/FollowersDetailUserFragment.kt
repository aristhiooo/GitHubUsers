package com.dicoding.aristiyo.githubusers.ui.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aristiyo.githubusers.databinding.FragmentFollowersDetailUserBinding
import com.dicoding.aristiyo.githubusers.ui.adapters.FollowersAdapter
import com.dicoding.aristiyo.githubusers.viewmodels.ViewModelsWithKTX

class FollowersDetailUserFragment : Fragment() {

    private var _binding: FragmentFollowersDetailUserBinding? = null
    private val binding get() = _binding!!
    private val userFollowersListViewModel by viewModels<ViewModelsWithKTX>()
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(DetailUserActivity.KEY_USERNAME).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userFollowersListViewModel.doGetUserFollowers(username.toString())

        userFollowersListViewModel.dataFollowersDetailUsersUserResponse.observe(
            viewLifecycleOwner,
            { userfollowers ->
                binding.rvListFollowers.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = FollowersAdapter(arrayListOf()).also {
                        it.setData(userfollowers)
                    }
                }
            })

        userFollowersListViewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.loadingProgress.visibility = View.VISIBLE
                binding.rvListFollowers.visibility = View.GONE
            } else if (!it) {
                binding.loadingProgress.visibility = View.GONE
                binding.rvListFollowers.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}