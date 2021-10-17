package com.etwicaksono.submission2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class UsersListPagerAdapter(fm: Fragment, private val username: String) : FragmentStateAdapter(fm) {

    private val tab = listOf(
        "followers", "following"
    )

    override fun createFragment(position: Int): Fragment {
        return UsersListFragment.newInstance(tab[position], username)
    }

    override fun getItemCount(): Int {
        return tab.size
    }
}