package com.home.artz.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.home.artz.model.datamodel.Artwork

@Database(
    entities = [Artwork::class],
    version = 1
)
@TypeConverters(ArtzTypeConverter::class)
abstract class ArtzDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}