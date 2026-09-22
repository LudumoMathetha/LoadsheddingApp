package com.example.loadsheddingapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.loadsheddingapp.data.local.dao.ScheduleDao
import com.example.loadsheddingapp.data.local.dao.SuburbDao
import com.example.loadsheddingapp.data.local.dao.UserDao
import com.example.loadsheddingapp.data.local.entity.ScheduleEntity
import com.example.loadsheddingapp.data.local.entity.SuburbEntity
import com.example.loadsheddingapp.data.local.entity.UserEntity

// AppDatabase is the central Room database class for the application.
// Room provides local SQLite persistence so saved suburbs and schedule slots
// remain available offline even when there is no internet connection.
@Database(
    entities = [
        SuburbEntity::class,
        ScheduleEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Abstract methods providing access to each DAO interface.
    abstract fun suburbDao(): SuburbDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Returns a thread-safe singleton instance of the Room database.
        // Singleton pattern prevents creating multiple expensive database connections.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "loadshedding_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
