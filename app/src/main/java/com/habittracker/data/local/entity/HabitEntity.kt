package com.habittracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val category: String,
    val color: Int,
    val icon: String,
    val targetDays: String, // comma-separated e.g. "1,2,3"
    val reminderTime: String?,
    val createdAt: Long,
    val isActive: Boolean
)
