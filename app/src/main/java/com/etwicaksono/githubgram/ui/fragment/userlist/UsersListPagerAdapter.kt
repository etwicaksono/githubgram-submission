package com.etwicaksono.githubgram.ui.fragment.userlist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etwicaksono.githubgram.R

class UsersListPagerAdapter(fm: Fragment, private val username: String) : FragmentStateAdapter(fm) {

    private val tab = listOf(
        fm.getString(R.string.follower),fm.getString(R.string.following)
    )

    override fun createFragment(position: Int): Fragment {
        return UsersListFragment.newInstance(tab[position], username)
    }

    override fun getItemCount(): Int {
        return tab.size
    }
}