package com.etwicaksono.githubgram.ui.fragment.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.githubgram.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    private var _viewModel: FavoriteViewModel? = null
    private val viewModel get() = _viewModel

    private lateinit var mFavoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFavoriteAdapter = FavoriteAdapter(FavoriteAdapter.DeleteFavoriteListener { favorite ->
            viewModel?.delete(favorite)
        })

        val model: FavoriteViewModel by viewModels { FavoriteViewModel.Factory(requireActivity().application) }
        _viewModel = model

        binding?.apply {
            this@FavoriteFragment.setHasOptionsMenu(true)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (activity as AppCompatActivity).supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)

            rvUsers.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = mFavoriteAdapter
            }

        }



        viewModel?.apply {
            getAllFavorites().observe(viewLifecycleOwner, { favorites ->
                if (favorites != null) {
                    binding?.apply {
                        if (favorites.isNotEmpty()) {
                            tvEmpty.visibility = View.INVISIBLE
                        } else {
                            tvEmpty.visibility = View.VISIBLE
                        }
                    }
                    mFavoriteAdapter.setFavorites(favorites)
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val TAG = FavoriteFragment::class.java.simpleName
    }

}