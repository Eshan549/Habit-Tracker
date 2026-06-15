package com.habittracker.presentation.ui.addhabit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.habittracker.domain.model.HabitCategory
import com.habittracker.presentation.theme.HabitColors
import com.habittracker.util.DateUtils

val habitIcons = listOf(
    "💪","🏃","🧘","📚","🥗","😴","⚡","🎨","💧","🎯",
    "🌅","🧹","💊","🎵","💻","🌿","🚴","🏊","✍️","🤸"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    habitId: Long?,
    onBack: () -> Unit,
    viewModel: AddHabitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(habitId) {
        if (habitId != null) viewModel.loadHabit(habitId)
    }
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.isEditing) "Edit Habit" else "New Habit", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.saveHabit() },
                        enabled = uiState.name.isNotBlank()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Name
            SectionLabel("Habit Name")
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                placeholder = { Text("e.g. Morning meditation") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            // Description
            SectionLabel("Description (optional)")
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.updateDescription(it) },
                placeholder = { Text("What's this habit about?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                maxLines = 3
            )

            // Icon picker
            SectionLabel("Icon")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(habitIcons) { icon ->
                    val isSelected = icon == uiState.selectedIcon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color(uiState.selectedColor).copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) Color(uiState.selectedColor) else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { viewModel.updateIcon(icon) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(icon, fontSize = 22.sp)
                    }
                }
            }

            // Color picker
            SectionLabel("Color")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(HabitColors) { color ->
                    val isSelected = color.toArgb() == uiState.selectedColor
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.outline else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { viewModel.updateColor(color.toArgb()) }
                    ) {
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check, contentDescription = null,
                                tint = Color.White, modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }

            // Category
            SectionLabel("Category")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(HabitCategory.entries) { cat ->
                    FilterChip(
                        selected = cat == uiState.category,
                        onClick = { viewModel.updateCategory(cat) },
                        label = { Text("${cat.emoji} ${cat.displayName}") },
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // Target days
            SectionLabel("Repeat on")
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..7).forEach { day ->
                    val isSelected = day in uiState.targetDays
                    val color = Color(uiState.selectedColor)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) color else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { viewModel.toggleTargetDay(day) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            DateUtils.dayOfWeekLabel(day),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Reminder
            SectionLabel("Reminder")
            ReminderPicker(
                reminderTime = uiState.reminderTime,
                onTimeSelected = { viewModel.updateReminderTime(it) }
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ReminderPicker(
    reminderTime: String?,
    onTimeSelected: (String?) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var hour by remember { mutableStateOf(8) }
    var minute by remember { mutableStateOf(0) }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Set Reminder Time") },
            text = {
                Column {
                    Text("Hour: $hour")
                    Slider(
                        value = hour.toFloat(),
                        onValueChange = { hour = it.toInt() },
                        valueRange = 0f..23f, steps = 22
                    )
                    Text("Minute: ${minute.toString().padStart(2, '0')}")
                    Slider(
                        value = minute.toFloat(),
                        onValueChange = { minute = (it / 5).toInt() * 5 },
                        valueRange = 0f..55f, steps = 10
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onTimeSelected("${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}")
                    showTimePicker = false
                }) { Text("Set") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            }
        )
    }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (reminderTime != null) {
            AssistChip(
                onClick = { showTimePicker = true },
                label = { Text("⏰ $reminderTime") },
                shape = RoundedCornerShape(10.dp)
            )
            TextButton(onClick = { onTimeSelected(null) }) { Text("Remove") }
        } else {
            OutlinedButton(
                onClick = { showTimePicker = true },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("+ Add Reminder")
            }
        }
    }
}
