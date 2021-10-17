package com.etwicaksono.submission2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.etwicaksono.submission2.databinding.FragmentDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class UserDetailFragment : Fragment() {

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String
    private lateinit var viewModel: UserDetailViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)

        val sectionsPagerAdapter = UsersListPagerAdapter(this,username)
//        val sectionsPagerAdapter = UsersListPagerAdapter(this,"etwicaksono")
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
            userData.observe(viewLifecycleOwner, { userData -> setUserData(userData) })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        username =
            UserDetailFragmentArgs.fromBundle(arguments as Bundle).username.let { if (it.isEmpty()) "etwicaksono" else it }

        val model: UserDetailViewModel by viewModels { UserDetailViewModel.Factory(username) }
        viewModel = model
    }

    private fun setUserData(userData: ResponseUserDetail?) {

        binding.apply {
            Glide.with(imgUser.context)
                .load(userData?.avatarUrl)
                .into(imgUser)

            toolbarTitle.text = "@" + userData?.login
            tvRepository.text = numberFormat(userData?.publicRepos.toString())
            tvFollower.text = numberFormat(userData?.followers.toString())
            tvFollowing.text = numberFormat(userData?.following.toString())

            userData?.company.let {
                if (it.isNullOrEmpty()) {
                    wrapperCompany.visibility = View.INVISIBLE
                } else {
                    wrapperCompany.visibility = View.VISIBLE
                    tvCompany.text = userData?.company
                }
            }

            userData?.location.let {
                if (it.isNullOrEmpty()) {
                    wrapperLocation.visibility = View.INVISIBLE
                } else {
                    wrapperLocation.visibility = View.VISIBLE
                    tvLocation.text = userData?.location
                }
            }

            userData?.htmlUrl.let {
                if (it.isNullOrEmpty()) {
                    wrapperHtmlUrl.visibility = View.INVISIBLE
                } else {
                    wrapperHtmlUrl.visibility = View.VISIBLE
                    tvHtmlUrl.text = userData?.htmlUrl
                }
            }

        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarWrapper.progressBar.visibility =
                View.VISIBLE
            binding.mainWrapper.visibility =
                View.INVISIBLE
        } else {
            binding.progressBarWrapper.progressBar.visibility =
                View.INVISIBLE
            binding.mainWrapper.visibility =
                View.VISIBLE
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
}
