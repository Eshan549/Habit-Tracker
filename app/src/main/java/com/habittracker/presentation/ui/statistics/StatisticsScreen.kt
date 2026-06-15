package com.habittracker.presentation.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.habittracker.util.DateUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    habitId: Long,
    onBack: () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(habitId) { viewModel.loadHabit(habitId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.habitWithStats?.habit?.name ?: "Statistics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val hw = uiState.habitWithStats ?: return@Scaffold
        val habit = hw.habit
        val habitColor = Color(habit.color)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = habitColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(habit.icon, fontSize = 28.sp)
                    }
                    Column {
                        Text(habit.name, style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold, color = Color.White)
                        Text("${habit.category.emoji} ${habit.category.displayName}",
                            style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }

            // Stats grid
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("🔥 Current\nStreak", "${hw.currentStreak} days", habitColor, Modifier.weight(1f))
                StatCard("🏆 Longest\nStreak", "${hw.longestStreak} days", habitColor, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("✅ Total\nCompletions", "${hw.totalCompletions}", habitColor, Modifier.weight(1f))
                StatCard("📊 Success\nRate", "${(hw.completionRate * 100).toInt()}%", habitColor, Modifier.weight(1f))
            }

            // 30-day calendar heatmap
            Text("Last 30 Days", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            CalendarHeatmap(last30Days = uiState.last30Days, habitColor = habitColor)

            // Weekly pattern
            Text("Weekly Pattern", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            WeeklyBar(last7Days = hw.last7Days, habitColor = habitColor)
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun CalendarHeatmap(last30Days: List<String>, habitColor: Color) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val completedSet = last30Days.toSet()
    val today = LocalDate.now()

    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )) {
        Column(modifier = Modifier.padding(16.dp)) {
            val days = (29 downTo 0).map { today.minusDays(it.toLong()) }
            val rows = days.chunked(7)
            rows.forEach { week ->
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(bottom = 6.dp)) {
                    week.forEach { date ->
                        val isCompleted = date.format(formatter) in completedSet
                        val isToday = date == today
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when {
                                        isCompleted -> habitColor
                                        isToday -> habitColor.copy(alpha = 0.2f)
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                date.dayOfMonth.toString(),
                                fontSize = 11.sp,
                                color = if (isCompleted) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyBar(last7Days: List<Boolean>, habitColor: Color) {
    val labels = DateUtils.last7DayLabels()
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            last7Days.forEachIndexed { i, done ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(80.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .fillMaxHeight(if (done) 1f else 0.15f)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(if (done) habitColor else MaterialTheme.colorScheme.surface)
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(labels.getOrElse(i) { "" }, style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
