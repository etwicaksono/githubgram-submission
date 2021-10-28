package com.etwicaksono.githubgram.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(mFavorite:Favorite)

    @Delete
    fun delete(mFavorite:Favorite?)

    @Query("SELECT * FROM favorite ORDER BY id ASC")
    fun getAllFavorites():LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite WHERE username = :username")
    fun getFavoriteByUsername(username:String):LiveData<Favorite>
}