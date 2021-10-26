package com.etwicaksono.githubgram.ui.fragment.home

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
import com.etwicaksono.githubgram.R
import com.etwicaksono.githubgram.databinding.FragmentUserHomeBinding
import com.etwicaksono.githubgram.responses.ResponseUserItem
import com.etwicaksono.githubgram.ui.fragment.userlist.UsersListViewModel
import kotlinx.coroutines.*


class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: UsersListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val model: UsersListViewModel by viewModels()
        viewModel = model
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.inflateMenu(R.menu.menu_main)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            binding?.toolbar?.menu?.findItem(R.id.search)?.actionView as androidx.appcompat.widget.SearchView

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
                            viewModel.isLoading.observe(viewLifecycleOwner,
                                { isLoading -> showLoading(isLoading) })
                        }
                    }
                }
                return false
            }

        })

        binding?.toolbar?.apply {
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

        binding?.rvUsers?.apply {
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        viewModel.apply {
            listUsers.observe(viewLifecycleOwner, { listUser -> setUsersData(listUser) })
            isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
            context?.let {
                hasInternet(it).observe(viewLifecycleOwner,
                    { internet -> checkInternet(internet) })
            }
            errorMessage.observe(viewLifecycleOwner, { error -> showError(error) })
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

    private fun setUsersData(listUser: List<ResponseUserItem>?) {
        val adapter = listUser?.let { UserHomeAdapter(it) }?.apply { notifyDataSetChanged() }
        binding?.rvUsers?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        private val TAG = UserHomeFragment::class.java.simpleName
    }

}