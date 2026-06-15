package com.habittracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.habittracker.data.local.dao.HabitCompletionDao
import com.habittracker.data.local.dao.HabitDao
import com.habittracker.data.local.entity.HabitCompletionEntity
import com.habittracker.data.local.entity.HabitEntity

@Database(
    entities = [HabitEntity::class, HabitCompletionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionDao(): HabitCompletionDao
}
