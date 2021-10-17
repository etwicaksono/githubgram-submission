package com.etwicaksono.submission2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.etwicaksono.submission2.databinding.FragmentDetailUserBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.reflect.KProperty

class UserDetailFragment : Fragment() {

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel : UserDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        viewModel.apply {
            this.isLoading.observe(viewLifecycleOwner, { isLoading -> showLoading(isLoading) })
            this.userData.observe(viewLifecycleOwner, { userData -> setUserData(userData) })
        }
    }

    private fun setUserData(userData: ResponseUserDetail?) {

        binding.apply {
            Glide.with(imgUser.context)
                .load(userData?.avatarUrl)
                .into(imgUser)

            toolbarTitle.text=userData?.login
            tvRepository.text = userData?.publicRepos.let { numberFormat(it.toString()) }
            tvFollower.text = userData?.followers.let { numberFormat(it.toString()) }
            tvFollowing.text = userData?.following.let { numberFormat(it.toString()) }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarWrapper.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    fun numberFormat(value: String, useSymbol: Boolean = false): String {
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
