package com.habittracker.data.local.dao

import androidx.room.*
import com.habittracker.data.local.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCompletionDao {

    /** Flow of ALL completions — used to trigger recomposition on any change */
    @Query("SELECT * FROM habit_completions ORDER BY completedAt DESC")
    fun getAllCompletionsFlow(): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completedAt DESC")
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE dateString = :date")
    suspend fun getCompletionsForDate(date: String): List<HabitCompletionEntity>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND dateString = :date LIMIT 1")
    suspend fun getCompletion(habitId: Long, date: String): HabitCompletionEntity?

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY dateString DESC")
    suspend fun getAllCompletionsForHabit(habitId: Long): List<HabitCompletionEntity>

    @Query("SELECT COUNT(*) FROM habit_completions WHERE habitId = :habitId")
    suspend fun getTotalCompletions(habitId: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(completion: HabitCompletionEntity): Long

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND dateString = :date")
    suspend fun deleteCompletion(habitId: Long, date: String)

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId AND dateString >= :fromDate ORDER BY dateString ASC")
    suspend fun getCompletionsSince(habitId: Long, fromDate: String): List<HabitCompletionEntity>
}
