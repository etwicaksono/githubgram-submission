package com.etwicaksono.submission2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.submission2.databinding.FragmentUserListBinding
import com.etwicaksono.submission2.databinding.ProgressBarBinding
import com.etwicaksono.submission2.databinding.ToolbarMainBinding


class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserListViewModel by viewModels()

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

        val layoutManager = LinearLayoutManager(context)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        viewModel.listUser.observe(viewLifecycleOwner, { listUser -> setUsersData(listUser) })

        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.progressBar.visibility=if(isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setUsersData(listUser: List<ResponseUserItem>?) {
        val adapter = listUser?.let { UserListAdapter(it) }
        binding.rvUsers.adapter = adapter
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}