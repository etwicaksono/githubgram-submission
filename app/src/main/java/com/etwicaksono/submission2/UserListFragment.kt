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
import com.etwicaksono.submission2.databinding.FragmentUserListBinding


class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserListViewModel by viewModels()

    companion object {
        private val TAG = UserListFragment::class.java.simpleName
    }

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

        binding.toolbar.inflateMenu(R.menu.menu_main)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            binding.toolbar.menu.findItem(R.id.search).actionView as androidx.appcompat.widget.SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.input_username)
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Submited: $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(context, "Changed: $newText", Toast.LENGTH_SHORT).show()
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
                            UserListFragmentDirections.actionUserListFragmentToDetailUserFragment()
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
            this.addItemDecoration(itemDecoration)
        }

        viewModel.apply {
            this.listUser.observe(viewLifecycleOwner, { listUser -> setUsersData(listUser) })
            this.isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
        }


    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBarWrapper.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.INVISIBLE
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