package com.home.artz.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.home.artz.model.datamodel.Artwork

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM FavoriteArtworks")
    suspend fun getFavorites(): List<Artwork>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artwork: Artwork)

    @Delete
    suspend fun delete(artwork: Artwork)
}