package com.etwicaksono.githubgram.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.etwicaksono.githubgram.R
import com.etwicaksono.githubgram.database.Favorite
import com.etwicaksono.githubgram.databinding.FragmentDetailUserBinding
import com.etwicaksono.githubgram.responses.ResponseUserDetail
import com.etwicaksono.githubgram.ui.fragment.favorite.FavoriteViewModel
import com.etwicaksono.githubgram.ui.fragment.userlist.UsersListPagerAdapter
import com.etwicaksono.githubgram.ui.fragment.userlist.UsersListViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class UserDetailFragment : Fragment() {

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding
    private lateinit var username: String
    private lateinit var userViewModel: UsersListViewModel
    private lateinit var favViewModel: FavoriteViewModel
    private var dataUser: ResponseUserDetail? = null
    private var favorite: Favorite? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            this@UserDetailFragment.setHasOptionsMenu(true)
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            val actionBar = (activity as AppCompatActivity).supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(true)
            collapsingToolbar.title = getString(R.string.app_name)
            toolbarTextAppearence()

            val sectionsPagerAdapter = UsersListPagerAdapter(this@UserDetailFragment, username)
            viewPager.adapter = sectionsPagerAdapter
            let {
                TabLayoutMediator(it.tabs, it.viewPager) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()
            }

            fabFavorite.setOnClickListener {
                val fav = Favorite()
                fav.apply {
                    username = dataUser?.login
                    avatar = dataUser?.avatarUrl
                }

                if (favorite != null) {
                    favViewModel.delete(favorite)
                } else {
                    favViewModel.insert(fav)
                }
            }

        }

        userViewModel.apply {
            userViewModel.getUserData(username)
            isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
            userData.observe(viewLifecycleOwner, { userData -> setUserData(userData) })
        }

        favViewModel.apply {
            getFavoriteByUsername(username).observe(viewLifecycleOwner,
                { favorite -> setFavorite(favorite) })
        }
    }

    private fun setFavorite(fav: Favorite?) {
        favorite = fav
        if (fav != null) {
            binding?.fabFavorite?.setImageResource(R.drawable.ic_favorite_red_48)
        } else {
            binding?.fabFavorite?.setImageResource(R.drawable.ic_favorite_border_black_48)
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

    private fun toolbarTextAppearence() {
        binding?.collapsingToolbar?.apply {
            setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
            setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        username =
            UserDetailFragmentArgs.fromBundle(arguments as Bundle).username

        val model1: UsersListViewModel by viewModels()
        userViewModel = model1

        val model2: FavoriteViewModel by viewModels { FavoriteViewModel.Factory(requireActivity().application) }
        favViewModel = model2
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUserData(userData: ResponseUserDetail?) {
        dataUser = userData

        binding?.apply {
            Glide.with(imgUser.context)
                .load(userData?.avatarUrl)
                .into(imgUser)

            toolbarTitle.text = "@${userData?.login}"
            tvName.text = userData?.name
            tvRepository.text = numberFormat(userData?.publicRepos.toString())
            tvFollower.text = numberFormat(userData?.followers.toString())
            tvFollowing.text = numberFormat(userData?.following.toString())

            userData?.company.let {
                if (it.isNullOrEmpty()) {
                    tvCompany.visibility = View.GONE
                } else {
                    tvCompany.visibility = View.VISIBLE
                    tvCompany.text = userData?.company
                }
            }

            userData?.location.let {
                if (it.isNullOrEmpty()) {
                    tvLocation.visibility = View.GONE
                } else {
                    tvLocation.visibility = View.VISIBLE
                    tvLocation.text = userData?.location
                }
            }

            userData?.htmlUrl.let {
                if (it.isNullOrEmpty()) {
                    tvHtmlUrl.visibility = View.GONE
                } else {
                    tvHtmlUrl.visibility = View.VISIBLE
                    tvHtmlUrl.text = userData?.htmlUrl
                }
            }

        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.apply {
                progressBarWrapper.progressBar.visibility =
                    View.VISIBLE
                infoWrapper.visibility =
                    View.INVISIBLE
                tabs.visibility =
                    View.INVISIBLE
                viewPager.visibility =
                    View.INVISIBLE
            }

        } else {
            binding?.apply {
                progressBarWrapper.progressBar.visibility =
                    View.INVISIBLE
                infoWrapper.visibility =
                    View.VISIBLE
                tabs.visibility =
                    View.VISIBLE
                viewPager.visibility =
                    View.VISIBLE
            }
        }
    }

    private fun numberFormat(value: String, useSymbol: Boolean = false): String {
        val result: Any
        if (useSymbol) {
            val local = Locale("id", "ID")
            val cursIndo = NumberFormat.getCurrencyInstance(local) as DecimalFormat
            val symbol = Currency.getInstance(local).getSymbol(local)
            cursIndo.positivePrefix = "$symbol. "
            result = cursIndo.format(value.toDouble())

        } else {
            result = NumberFormat.getInstance().format(value.toDouble())
        }

        return result.toString()

    }


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    }

}
