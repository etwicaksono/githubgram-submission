package com.etwicaksono.githubgram.ui.fragment.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.githubgram.R
import com.etwicaksono.githubgram.databinding.FragmentUserHomeBinding
import com.etwicaksono.githubgram.ui.fragment.userlist.UsersListViewModel
import kotlinx.coroutines.*


class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: UsersListViewModel
    private lateinit var mHomeAdapter: UserHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val model: UsersListViewModel by viewModels()
        viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            toolbar.inflateMenu(R.menu.menu_main)
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.about -> {
                        val toDetailUserFragment =
                            UserHomeFragmentDirections.actionUserListFragmentToDetailUserFragment()
                        Navigation.findNavController(toolbar).navigate(toDetailUserFragment)
                    }
                    R.id.favorites -> {
                        val toFavoriteUsersFragment =
                            UserHomeFragmentDirections.actionUserListFragmentToFavoriteFragment()
                        Navigation.findNavController(toolbar).navigate(toFavoriteUsersFragment)
                    }
                    R.id.setting -> {
                        val toSettingFragment =
                            UserHomeFragmentDirections.actionUserListFragmentToSettingFragment()
                        Navigation.findNavController(toolbar).navigate(toSettingFragment)
                    }
                }
                true
            }


            val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchView =
                toolbar.menu?.findItem(R.id.search)?.actionView as androidx.appcompat.widget.SearchView

            searchView.apply {
                setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
                queryHint = resources.getString(R.string.input_username)
                setOnQueryTextListener(object :
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
                                    viewModel.isLoading.observe(viewLifecycleOwner,
                                        { isLoading -> showLoading(isLoading) })
                                }
                            }
                        }
                        return false
                    }

                })
            }
        }


        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        mHomeAdapter = UserHomeAdapter()
        binding?.rvUsers?.apply {
            adapter = mHomeAdapter
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        viewModel.apply {
            listUsers.observe(viewLifecycleOwner) { listUser ->
                if (listUser != null) {
                    if (listUser.isNotEmpty()) {
                        binding?.tvEmpty?.visibility = View.INVISIBLE
                    } else {
                        binding?.tvEmpty?.visibility = View.VISIBLE
                    }
                    mHomeAdapter.setListUsersData(listUser)
                }
            }
            isLoading.observe(viewLifecycleOwner) { isLoading -> showLoading(isLoading) }
            context?.let {
                hasInternet(it).observe(viewLifecycleOwner
                ) { internet -> checkInternet(internet) }
            }
            errorMessage.observe(viewLifecycleOwner) { error -> showError(error) }
        }
    }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    private fun checkInternet(internet: Boolean) {
        if (!internet) {
            Toast.makeText(context, getString(R.string.internet_unavailable), Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding?.progressBarWrapper?.progressBar?.visibility =
            if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}