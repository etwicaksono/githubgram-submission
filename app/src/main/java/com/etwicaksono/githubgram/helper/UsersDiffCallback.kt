package com.etwicaksono.githubgram.helper

import androidx.recyclerview.widget.DiffUtil
import com.etwicaksono.githubgram.responses.ResponseUserItem

class UsersDiffCallback(
    private val mOldUsersList: List<ResponseUserItem>,
    private val mNewUsersList: List<ResponseUserItem>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldUsersList.size
    }

    override fun getNewListSize(): Int {
        return mNewUsersList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUsersList[oldItemPosition].login == mNewUsersList[newItemPosition].login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldUsersList[oldItemPosition]
        val newUser = mNewUsersList[newItemPosition]
        return oldUser.login == newUser.login && oldUser.avatarUrl == newUser.avatarUrl
    }
}