package com.etwicaksono.githubgram.ui.fragment.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etwicaksono.githubgram.database.Favorite
import com.etwicaksono.githubgram.databinding.ItemRowUserFavoriteBinding
import com.etwicaksono.githubgram.helper.FavoritesDiffCallback

class FavoriteAdapter(val clickListener: DeleteFavoriteListener) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemRowUserFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            with(binding) {
                Glide.with(imgUser)
                    .load(favorite.avatar)
                    .into(imgUser)
                tvName.text = favorite.username
                itemRowUserFav.setOnClickListener { view ->
                    val toDetailUserFragment =
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailUserFragment()
                    toDetailUserFragment.username = favorite.username.toString()
                    view.findNavController().navigate(toDetailUserFragment)
                }
                btnDeleteFav.setOnClickListener {
                    clickListener.onClick(favorite)
                }
            }
        }
    }

    class DeleteFavoriteListener(val clickListener: (favorite: Favorite) -> Unit) {
        fun onClick(favorite: Favorite) = clickListener(favorite)
    }

    private val listFavorites = ArrayList<Favorite>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowUserFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavorites[position])
    }

    override fun getItemCount(): Int {
        return listFavorites.size
    }

    fun setFavorites(favorites: List<Favorite>) {
        val diffCallback = FavoritesDiffCallback(this.listFavorites, favorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.apply {
            clear()
            addAll(favorites)
        }
        diffResult.dispatchUpdatesTo(this)

    }
}