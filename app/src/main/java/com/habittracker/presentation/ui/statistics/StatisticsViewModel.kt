package com.habittracker.presentation.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.data.repository.HabitRepository
import com.habittracker.domain.model.HabitWithStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatisticsUiState(
    val habitWithStats: HabitWithStats? = null,
    val last30Days: List<String> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    fun loadHabit(habitId: Long) {
        viewModelScope.launch {
            val habitWithStats = repository.getHabitWithStats(habitId)
            val last30Days = repository.getCompletionsLast30Days(habitId)
            _uiState.value = StatisticsUiState(
                habitWithStats = habitWithStats,
                last30Days = last30Days,
                isLoading = false
            )
        }
    }
}
