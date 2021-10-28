package com.etwicaksono.githubgram.ui.fragment.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.etwicaksono.githubgram.database.Favorite
import com.etwicaksono.githubgram.repository.FavoriteRepository

class FavoriteViewModel(application: Application):ViewModel() {
    private val mFavoriteRepository:FavoriteRepository= FavoriteRepository(application)

    fun getAllNotes():LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorites()
}