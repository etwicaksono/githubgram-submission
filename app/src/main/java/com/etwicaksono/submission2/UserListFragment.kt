package com.etwicaksono.submission2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.submission2.databinding.FragmentUserListBinding


class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager=LinearLayoutManager(context)
        binding.rvUsers.layoutManager=layoutManager
        val itemDecoration=DividerItemDecoration(context,layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        getAllUsers()
    }

    private fun getAllUsers() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}