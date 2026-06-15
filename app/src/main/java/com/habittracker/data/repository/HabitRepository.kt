package com.habittracker.data.repository

import com.habittracker.data.local.dao.HabitCompletionDao
import com.habittracker.data.local.dao.HabitDao
import com.habittracker.data.local.entity.HabitCompletionEntity
import com.habittracker.data.local.entity.HabitEntity
import com.habittracker.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val completionDao: HabitCompletionDao
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * Combines the habits flow AND completions flow so that toggling a completion
     * causes the combined flow to re-emit — fixing the stale-state toggle bug.
     */
    fun getAllHabitsWithStats(): Flow<List<HabitWithStats>> {
        return combine(
            habitDao.getAllActiveHabits(),
            completionDao.getAllCompletionsFlow()
        ) { habits, _ ->
            // Second param ignored — we just need the trigger to recompute
            habits.map { entity -> buildHabitWithStats(entity.toDomain()) }
        }
    }

    suspend fun getHabitWithStats(habitId: Long): HabitWithStats? {
        val entity = habitDao.getHabitById(habitId) ?: return null
        return buildHabitWithStats(entity.toDomain())
    }

    private suspend fun buildHabitWithStats(habit: Habit): HabitWithStats {
        val today = LocalDate.now().format(dateFormatter)
        val allCompletions = completionDao.getAllCompletionsForHabit(habit.id)
        val completedDates = allCompletions.map { it.dateString }.toSet()
        val isCompletedToday = today in completedDates
        val totalCompletions = allCompletions.size

        val last7Days = (6 downTo 0).map { daysAgo ->
            val date = LocalDate.now().minusDays(daysAgo.toLong()).format(dateFormatter)
            date in completedDates
        }

        val currentStreak = calculateCurrentStreak(completedDates, habit.targetDays)
        val longestStreak = calculateLongestStreak(completedDates, habit.targetDays)

        val targetDayCount = if (habit.targetDays.size == 7 || habit.targetDays.isEmpty()) 7 else habit.targetDays.size
        val weeksPassed = maxOf(1, ((System.currentTimeMillis() - habit.createdAt) / (7 * 24 * 3600 * 1000L)).toInt() + 1)
        val expectedCompletions = weeksPassed * targetDayCount
        val completionRate = if (expectedCompletions > 0)
            (totalCompletions.toFloat() / expectedCompletions).coerceIn(0f, 1f) else 0f

        return HabitWithStats(
            habit = habit,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            completionRate = completionRate,
            totalCompletions = totalCompletions,
            isCompletedToday = isCompletedToday,
            last7Days = last7Days
        )
    }

    private fun calculateCurrentStreak(completedDates: Set<String>, targetDays: List<Int>): Int {
        var streak = 0
        var date = LocalDate.now()
        repeat(365) {
            val dayOfWeek = date.dayOfWeek.value
            if (targetDays.isEmpty() || dayOfWeek in targetDays) {
                if (date.format(dateFormatter) in completedDates) streak++
                else return streak
            }
            date = date.minusDays(1)
        }
        return streak
    }

    private fun calculateLongestStreak(completedDates: Set<String>, targetDays: List<Int>): Int {
        if (completedDates.isEmpty()) return 0
        val sorted = completedDates.sorted()
        var longest = 0; var current = 0; var prevDate: LocalDate? = null
        for (dateStr in sorted) {
            val date = LocalDate.parse(dateStr, dateFormatter)
            if (targetDays.isEmpty() || date.dayOfWeek.value in targetDays) {
                current = if (prevDate == null || date == prevDate!!.plusDays(1)) current + 1 else 1
                longest = maxOf(longest, current)
                prevDate = date
            }
        }
        return longest
    }

    suspend fun toggleHabitCompletion(habitId: Long, date: String) {
        val existing = completionDao.getCompletion(habitId, date)
        if (existing != null) {
            completionDao.deleteCompletion(habitId, date)
        } else {
            completionDao.insertCompletion(
                HabitCompletionEntity(
                    habitId = habitId,
                    completedAt = System.currentTimeMillis(),
                    dateString = date
                )
            )
        }
    }

    suspend fun insertHabit(habit: Habit): Long = habitDao.insertHabit(habit.toEntity())
    suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit.toEntity())
    suspend fun deleteHabit(habitId: Long) = habitDao.softDeleteHabit(habitId)

    suspend fun getCompletionsLast30Days(habitId: Long): List<String> {
        val fromDate = LocalDate.now().minusDays(30).format(dateFormatter)
        return completionDao.getCompletionsSince(habitId, fromDate).map { it.dateString }
    }

    private fun HabitEntity.toDomain(): Habit = Habit(
        id = id, name = name, description = description,
        category = HabitCategory.valueOf(category),
        color = color, icon = icon,
        targetDays = if (targetDays.isBlank()) emptyList()
                     else targetDays.split(",").map { it.trim().toInt() },
        reminderTime = reminderTime, createdAt = createdAt, isActive = isActive
    )

    private fun Habit.toEntity(): HabitEntity = HabitEntity(
        id = id, name = name, description = description,
        category = category.name, color = color, icon = icon,
        targetDays = targetDays.joinToString(","),
        reminderTime = reminderTime, createdAt = createdAt, isActive = isActive
    )
}
