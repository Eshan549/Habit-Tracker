package com.habittracker.presentation.ui.addhabit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.data.repository.HabitRepository
import com.habittracker.domain.model.Habit
import com.habittracker.domain.model.HabitCategory
import com.habittracker.presentation.theme.HabitColors
import com.habittracker.util.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddHabitUiState(
    val name: String = "",
    val description: String = "",
    val category: HabitCategory = HabitCategory.OTHER,
    val selectedColor: Int = HabitColors[0].hashCode(),
    val selectedIcon: String = "✨",
    val targetDays: List<Int> = listOf(1, 2, 3, 4, 5),
    val reminderTime: String? = null,
    val isEditing: Boolean = false,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val repository: HabitRepository,
    private val notificationScheduler: NotificationScheduler,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddHabitUiState())
    val uiState: StateFlow<AddHabitUiState> = _uiState.asStateFlow()

    private var editingHabitId: Long? = null

    fun loadHabit(habitId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val habitWithStats = repository.getHabitWithStats(habitId)
            if (habitWithStats != null) {
                val h = habitWithStats.habit
                editingHabitId = h.id
                _uiState.value = AddHabitUiState(
                    name = h.name, description = h.description,
                    category = h.category, selectedColor = h.color,
                    selectedIcon = h.icon, targetDays = h.targetDays,
                    reminderTime = h.reminderTime, isEditing = true, isLoading = false
                )
            }
        }
    }

    fun updateName(name: String) { _uiState.value = _uiState.value.copy(name = name) }
    fun updateDescription(desc: String) { _uiState.value = _uiState.value.copy(description = desc) }
    fun updateCategory(cat: HabitCategory) { _uiState.value = _uiState.value.copy(category = cat) }
    fun updateColor(color: Int) { _uiState.value = _uiState.value.copy(selectedColor = color) }
    fun updateIcon(icon: String) { _uiState.value = _uiState.value.copy(selectedIcon = icon) }
    fun updateReminderTime(time: String?) { _uiState.value = _uiState.value.copy(reminderTime = time) }

    fun toggleTargetDay(day: Int) {
        val current = _uiState.value.targetDays.toMutableList()
        if (day in current) current.remove(day) else current.add(day)
        _uiState.value = _uiState.value.copy(targetDays = current.sorted())
    }

    fun saveHabit() {
        val state = _uiState.value
        if (state.name.isBlank()) return
        viewModelScope.launch {
            val habit = Habit(
                id = editingHabitId ?: 0,
                name = state.name.trim(),
                description = state.description.trim(),
                category = state.category,
                color = state.selectedColor,
                icon = state.selectedIcon,
                targetDays = state.targetDays,
                reminderTime = state.reminderTime
            )
            if (editingHabitId != null) {
                repository.updateHabit(habit)
                notificationScheduler.cancelHabitReminder(context, habit.id)
            } else {
                val newId = repository.insertHabit(habit)
                val savedHabit = habit.copy(id = newId)
                if (savedHabit.reminderTime != null) {
                    notificationScheduler.scheduleHabitReminder(context, savedHabit)
                }
            }
            _uiState.value = _uiState.value.copy(isSaved = true)
        }
    }
}
