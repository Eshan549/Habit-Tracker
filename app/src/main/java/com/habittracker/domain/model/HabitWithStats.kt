package com.habittracker.domain.model

data class HabitWithStats(
    val habit: Habit,
    val currentStreak: Int,
    val longestStreak: Int,
    val completionRate: Float,
    val totalCompletions: Int,
    val isCompletedToday: Boolean,
    val last7Days: List<Boolean>
)
