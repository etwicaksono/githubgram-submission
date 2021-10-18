package com.etwicaksono.submission2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etwicaksono.submission2.databinding.ItemRowUserBinding

class UserHomeAdapter(private val listUser: List<ResponseUserItem>) :
    RecyclerView.Adapter<UserHomeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRowUserBinding.bind(view)
    }

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

        holder.binding.itemRowUser.setOnClickListener { view ->
            val toDetailUserFragment =
                UserHomeFragmentDirections.actionUserListFragmentToDetailUserFragment()
            toDetailUserFragment.username = listUser[position].login
            view.findNavController().navigate(toDetailUserFragment)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }
}