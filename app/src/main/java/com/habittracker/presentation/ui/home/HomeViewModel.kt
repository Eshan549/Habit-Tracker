package com.habittracker.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.data.repository.HabitRepository
import com.habittracker.domain.model.HabitWithStats
import com.habittracker.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val habitsWithStats: List<HabitWithStats> = emptyList(),
    val isLoading: Boolean = true,
    val completedToday: Int = 0,
    val totalToday: Int = 0
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Flow-based: re-emits any time DB changes (toggle, add, delete)
        viewModelScope.launch {
            repository.getAllHabitsWithStats().collect { habits ->
                val todayDayOfWeek = LocalDate.now().dayOfWeek.value
                val todayHabits = habits.filter { hw ->
                    hw.habit.targetDays.isEmpty() || todayDayOfWeek in hw.habit.targetDays
                }
                _uiState.value = HomeUiState(
                    habitsWithStats = habits,
                    isLoading = false,
                    completedToday = todayHabits.count { it.isCompletedToday },
                    totalToday = todayHabits.size
                )
            }
        }
    }

    fun toggleCompletion(habitId: Long) {
        viewModelScope.launch {
            // Toggle in DB — the Flow above will automatically re-emit updated state
            repository.toggleHabitCompletion(habitId, DateUtils.today())
        }
    }

    fun deleteHabit(habitId: Long) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
        }
    }
}
