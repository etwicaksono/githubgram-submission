package com.etwicaksono.githubgram.ui.fragment.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etwicaksono.githubgram.R
import com.etwicaksono.githubgram.databinding.ItemRowUserBinding
import com.etwicaksono.githubgram.helper.UsersDiffCallback
import com.etwicaksono.githubgram.responses.ResponseUserItem

class UsersListAdapter :
    RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRowUserBinding.bind(view)
    }

    private val listUser= ArrayList<ResponseUserItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = listUser[position].login

            Glide.with(imgUser.context)
                .load(listUser[position].avatarUrl)
                .into(imgUser)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }


    fun setListUsersData(listUsers:List<ResponseUserItem>){
        val diffCallback= UsersDiffCallback(this.listUser,listUsers)
        val diffResult=DiffUtil.calculateDiff(diffCallback)
        listUser.clear()
        listUser.addAll(listUsers)
        diffResult.dispatchUpdatesTo(this)
    }
}