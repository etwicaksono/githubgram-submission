package com.etwicaksono.githubgram.ui.fragment.userlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.githubgram.R
import com.etwicaksono.githubgram.databinding.FragmentUsersListBinding


class UsersListFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding

    private lateinit var type: String
    private lateinit var username: String
    private lateinit var viewModel: UsersListViewModel
    private lateinit var mUsersListAdapter: UsersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        type = UsersListFragmentArgs.fromBundle(arguments as Bundle).type
        username = UsersListFragmentArgs.fromBundle(arguments as Bundle).username
        val model: UsersListViewModel by viewModels()
        viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        binding?.rvUsers?.apply {
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        mUsersListAdapter = UsersListAdapter()

        viewModel.apply {
            when (type) {
                getString(R.string.follower) -> {
                    viewModel.getFollowersData(username)
                    followers.observe(viewLifecycleOwner, { listUser ->
                        if (listUser != null) {
                            if (listUser.isNotEmpty()) {
                                binding?.tvEmpty?.visibility = View.INVISIBLE
                            } else {
                                binding?.tvEmpty?.visibility = View.VISIBLE
                            }
                            mUsersListAdapter.setListUsersData(listUser)
                        }
                    })
                }

                getString(R.string.following) -> {
                    viewModel.getFollowingData(username)
                    followings.observe(viewLifecycleOwner, { listUser ->
                        if (listUser != null) {
                            if (listUser.isNotEmpty()) {
                                binding?.tvEmpty?.visibility = View.INVISIBLE
                            } else {
                                binding?.tvEmpty?.visibility = View.VISIBLE
                            }
                            mUsersListAdapter.setListUsersData(listUser)
                        }
                    })
                }

                else -> {
                    getAllUsers()
                    listUsers.observe(viewLifecycleOwner, { listUser ->
                        if (listUser != null) {
                            if (listUser.isNotEmpty()) {
                                binding?.tvEmpty?.visibility = View.INVISIBLE
                            } else {
                                binding?.tvEmpty?.visibility = View.VISIBLE
                            }
                            mUsersListAdapter.setListUsersData(listUser)
                        }
                    })
                }
            }
            binding?.rvUsers?.adapter = mUsersListAdapter
            isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBarWrapper?.progressBar?.visibility =
                View.VISIBLE
        } else {
            binding?.progressBarWrapper?.progressBar?.visibility =
                View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        @JvmStatic
        fun newInstance(type: String, username: String): UsersListFragment =
            UsersListFragment().apply {
                arguments = Bundle().apply {
                    putString("type", type)
                    putString("username", username)
                }
            }
    }
}