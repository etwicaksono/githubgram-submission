package com.etwicaksono.githubgram.ui.fragment.userlist

import android.content.Context
import android.content.res.Resources
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
import com.etwicaksono.githubgram.responses.ResponseUserItem


class UsersListFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding

    private lateinit var type: String
    private lateinit var username: String
    private lateinit var viewModel: UsersListViewModel

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

        viewModel.apply {
            when (type) {
                getString(R.string.follower) -> {
                    viewModel.getFollowersData(username)
                    followers.observe(viewLifecycleOwner, { listUser ->
                        setUsersData(listUser)
                    })
                }

                getString(R.string.following) -> {
                    viewModel.getFollowingData(username)
                    followings.observe(viewLifecycleOwner, { listUser ->
                        setUsersData(listUser)
                    })
                }

                else -> {
                    getAllUsers()
                    listUsers.observe(viewLifecycleOwner, { listUser ->
                        setUsersData(listUser)
                    })
                }
            }
            isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
        }

    }

    private fun setUsersData(listUser: List<ResponseUserItem>?) {
        val adapter = listUser?.let { UsersListAdapter(it) }?.apply { notifyDataSetChanged() }
        binding?.rvUsers?.adapter = adapter
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