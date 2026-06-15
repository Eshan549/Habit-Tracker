package com.habittracker.domain.model

data class Habit(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val category: HabitCategory,
    val color: Int,
    val icon: String,
    val targetDays: List<Int>,
    val reminderTime: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

enum class HabitCategory(val displayName: String, val emoji: String) {
    HEALTH("Health", "💪"),
    MINDFULNESS("Mindfulness", "🧘"),
    LEARNING("Learning", "📚"),
    FITNESS("Fitness", "🏃"),
    NUTRITION("Nutrition", "🥗"),
    SLEEP("Sleep", "😴"),
    PRODUCTIVITY("Productivity", "⚡"),
    SOCIAL("Social", "👥"),
    CREATIVITY("Creativity", "🎨"),
    OTHER("Other", "✨")
}
