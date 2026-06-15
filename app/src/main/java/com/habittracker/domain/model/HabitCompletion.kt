package com.habittracker.domain.model

data class HabitCompletion(
    val id: Long = 0,
    val habitId: Long,
    val completedAt: Long = System.currentTimeMillis(),
    val dateString: String
)
