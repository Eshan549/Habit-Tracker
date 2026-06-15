package com.habittracker.di

import android.content.Context
import androidx.room.Room
import com.habittracker.data.local.HabitDatabase
import com.habittracker.data.local.dao.HabitCompletionDao
import com.habittracker.data.local.dao.HabitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HabitDatabase =
        Room.databaseBuilder(context, HabitDatabase::class.java, "habit_tracker_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideHabitDao(db: HabitDatabase): HabitDao = db.habitDao()

    @Provides
    fun provideCompletionDao(db: HabitDatabase): HabitCompletionDao = db.habitCompletionDao()
}
