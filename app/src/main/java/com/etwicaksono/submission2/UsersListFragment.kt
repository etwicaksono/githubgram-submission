package com.etwicaksono.submission2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.submission2.databinding.FragmentUsersListBinding


class UsersListFragment : Fragment() {

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    private lateinit var type: String
    private lateinit var username: String
    private lateinit var viewModel: UsersListViewModel

    companion object {
        @JvmStatic
        fun newInstance(type: String, username: String) =
            UsersListFragment().apply {
                arguments = Bundle().apply {
                    putString("type", type)
                    putString("username", username)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        type = UsersListFragmentArgs.fromBundle(arguments as Bundle).type
        username = UsersListFragmentArgs.fromBundle(arguments as Bundle).username
        val model: UsersListViewModel by viewModels { UsersListViewModel.Factory(type, username) }
        viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        binding.rvUsers.apply {
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        viewModel.followers.observe(viewLifecycleOwner, { listUser ->
            setUsersData(listUser)
        })
        viewModel.following.observe(viewLifecycleOwner, { listUser ->
            setUsersData(listUser)
        })

        viewModel.listUser.observe(viewLifecycleOwner, { listUser ->
            setUsersData(listUser)
        })


    }

    private fun setUsersData(listUser: List<ResponseUserItem>?) {
        val adapter = listUser?.let { UsersListAdapter(it) }
        binding.rvUsers.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}