package com.etwicaksono.githubgram.ui.fragment.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.etwicaksono.githubgram.database.Favorite
import com.etwicaksono.githubgram.repository.FavoriteRepository

class FavoriteViewModel(application: Application):ViewModel() {
    class Factory(private val application:Application): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoriteViewModel(application) as T
        }
    }

    private val mFavoriteRepository:FavoriteRepository= FavoriteRepository(application)

    fun getAllFavorites():LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorites()
}