package com.etwicaksono.githubgram.helper

import androidx.recyclerview.widget.DiffUtil
import com.etwicaksono.githubgram.database.Favorite

class FavoritesDiffCallback(
    private val mOldFavList: List<Favorite>,
    private val mNewFavList: List<Favorite>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFav = mOldFavList[oldItemPosition]
        val newFav = mNewFavList[newItemPosition]
        return oldFav.id == newFav.id && oldFav.username == newFav.username
    }
}