package com.etwicaksono.submission2

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.submission2.databinding.FragmentUserHomeBinding
import kotlinx.coroutines.*


class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UsersListViewModel

    companion object {
        private val TAG = UserHomeFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val model: UsersListViewModel by viewModels { UsersListViewModel.Factory("all") }
        viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.inflateMenu(R.menu.menu_main)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            binding.toolbar.menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.input_username)
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

            private var searchJob: Job? = null

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    newText?.let {
                        delay(500)
                        if (it.isEmpty()) {
                            viewModel.getAllUsers()
                        } else {
                            viewModel.searchUser(newText)
                            viewModel.loadingSearch.observe(viewLifecycleOwner,
                                { isLoading -> showLoading(isLoading) })
                        }
                    }
                }
                return false
            }

        })

        binding.toolbar.apply {
            this.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.search -> {
                        Log.d(TAG, "onItemSelected : search")
                    }
                    R.id.about -> {
                        val toDetailUserFragment =
                            UserHomeFragmentDirections.actionUserListFragmentToDetailUserFragment()
                        Navigation.findNavController(this).navigate(toDetailUserFragment)
                        Log.d(TAG, "onItemSelected : about")
                    }
                }
                true
            }
        }

        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        binding.rvUsers.apply {
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        viewModel.apply {
            listUser.observe(viewLifecycleOwner, { listUser -> setUsersData(listUser) })
            isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
            context?.let {
                hasInternet(it).observe(viewLifecycleOwner,
                    { internet -> checkInternet(internet) })
            }
        }


    }

    private fun checkInternet(internet: Boolean) {
        if (!internet) {
            Toast.makeText(context, "Internet tidak tersedia", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBarWrapper.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setUsersData(listUser: List<ResponseUserItem>?) {
        val adapter = listUser?.let { UserHomeAdapter(it) }?.apply { notifyDataSetChanged() }
        binding.rvUsers.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}