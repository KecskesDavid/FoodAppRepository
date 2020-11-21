package com.example.foodproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodproject.model.Restaurant

@Database(entities = [Restaurant::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context):AppDatabase{
            val tempInstance = INSTANCE
            if( tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Food_Database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}