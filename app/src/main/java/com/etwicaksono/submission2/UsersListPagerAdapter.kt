package com.etwicaksono.submission2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class UsersListPagerAdapter(fm: Fragment, private val username: String) : FragmentStateAdapter(fm) {

    companion object {
        private val TAB_TITLES = listOf(
//            Resources.getSystem().getString(R.string.tab_text_1),
//            Resources.getSystem().getString(R.string.tab_text_2)
            "Followers", "Following"
        )
    }

    override fun createFragment(position: Int): Fragment {
        return UsersListFragment(TAB_TITLES[position], username)
    }

    override fun getItemCount(): Int {
        return TAB_TITLES.size
    }
}