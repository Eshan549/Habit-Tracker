package com.habittracker.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun today(): String = LocalDate.now().format(formatter)

    fun dayOfWeekLabel(index: Int): String = when (index) {
        1 -> "Mon"; 2 -> "Tue"; 3 -> "Wed"; 4 -> "Thu"
        5 -> "Fri"; 6 -> "Sat"; 7 -> "Sun"; else -> ""
    }

    fun last7DayLabels(): List<String> = (6 downTo 0).map { daysAgo ->
        LocalDate.now().minusDays(daysAgo.toLong())
            .dayOfWeek.name.take(1)
    }
}
