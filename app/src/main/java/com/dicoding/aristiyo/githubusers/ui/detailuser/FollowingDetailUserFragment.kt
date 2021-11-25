package com.dicoding.aristiyo.githubusers.ui.detailuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aristiyo.githubusers.databinding.FragmentFollowingDetailUserBinding
import com.dicoding.aristiyo.githubusers.ui.adapters.FollowingsAdapter
import com.dicoding.aristiyo.githubusers.viewmodels.ViewModelsWithKTX

class FollowingDetailUserFragment : Fragment() {

    private var _binding: FragmentFollowingDetailUserBinding? = null
    private val binding get() = _binding!!
    private val userFollowingListViewModel by viewModels<ViewModelsWithKTX>()
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
        _binding = FragmentFollowingDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userFollowingListViewModel.doGetUserFollowings(username.toString())

        userFollowingListViewModel.dataFollowingsDetailUsersUserResponse.observe(
            viewLifecycleOwner,
            { userFollowings ->
                binding.rvListFollowing.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = FollowingsAdapter(arrayListOf()).also {
                        it.setData(userFollowings)
                    }
                }
            })

        userFollowingListViewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.loadingProgress.visibility = View.VISIBLE
                binding.rvListFollowing.visibility = View.GONE
            } else if (!it) {
                binding.loadingProgress.visibility = View.GONE
                binding.rvListFollowing.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}