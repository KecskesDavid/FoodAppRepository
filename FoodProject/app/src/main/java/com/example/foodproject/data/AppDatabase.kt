package com.example.foodproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Restaurant::class, FavoriteRestaurants::class, User::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val migration_1_2: Migration = object: Migration(1,2)
        {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE restaurants")
                database.execSQL("CREATE TABLE IF NOT EXISTS `restaurants` (`id` INTEGER NOT NULL,`name` TEXT NOT NULL,`address` TEXT NOT NULL,`city` TEXT NOT NULL,`state` TEXT NOT NULL,`area` TEXT NOT NULL,`postal_code` TEXT NOT NULL,`country` TEXT NOT NULL,`phone` TEXT NOT NULL,`lat` REAL NOT NULL,`lng` REAL NOT NULL,`price` INTEGER NOT NULL,`reserve_url` TEXT NOT NULL,`mobile_reserve_url` TEXT NOT NULL,`image_url` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("CREATE TABLE IF NOT EXISTS `favorite_restaurants` (`id` INTEGER NOT NULL ,`restaurant_id` INTEGER NOT NULL,`user_id` INTEGER NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        val migration_2_3: Migration = object: Migration(2,3)
        {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER NOT NULL,`name` TEXT NOT NULL,`email` TEXT NOT NULL,`address` TEXT NOT NULL,`phone` TEXT NOT NULL,`password` TEXT NOT NULL,`photo` TEXT NOT NULL, PRIMARY KEY(`id`))")
            }
        }

        val migration_3_4: Migration = object: Migration(3,4)
        {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `users` ADD COLUMN job TEXT NOT NULL DEFAULT ''")
            }
        }

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
                )
                        .addMigrations(migration_1_2, migration_2_3, migration_3_4)
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}