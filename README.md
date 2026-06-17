# HabitTracker 📋

A clean, modern Android habit tracking app built with **Jetpack Compose** and **Material 3**. Track daily habits, monitor streaks, visualize progress, and stay consistent with scheduled reminders.

---

## Screenshots

<img width="300" alt="Screenshot_2026-06-15-23-01-30-20" src="https://github.com/user-attachments/assets/675f3f3c-cad3-4ba1-b741-1358bfe4848d" />
<img width="300" alt="Screenshot_2026-06-15-23-01-34-46" src="https://github.com/user-attachments/assets/5b6059da-50cf-4043-9467-082f0a71852c" />
<img width="300" alt="Screenshot_2026-06-15-23-02-03-20" src="https://github.com/user-attachments/assets/bd39563d-8ac7-41f5-9c44-e16ed9adcb16" />


---

## Features

- ✅ **Daily habit tracking** — mark habits complete with a single tap
- 🔥 **Streak tracking** — current and longest streaks calculated automatically
- 📊 **Statistics** — 30-day heatmap, weekly bar chart, and completion rate per habit
- 🏷️ **Categories & icons** — 10 categories, 20 icons, 10 accent colors
- ⏰ **Reminders** — scheduled daily push notifications per habit via AlarmManager
- 🌙 **Theme toggle** — Light, Dark, or Follow Device theme, persisted across sessions
- 🗂️ **Habit management** — create, edit, and delete habits with custom repeat days

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, Clean Architecture |
| Database | Room (SQLite) |
| Dependency Injection | Hilt |
| Async / State | Kotlin Coroutines, StateFlow, Flow |
| Notifications | AlarmManager, BroadcastReceiver |
| Persistence | DataStore Preferences |
| Language | Kotlin |

---

## Architecture

The app follows **Clean Architecture** with three distinct layers:

```
com.habittracker/
├── domain/             # Models: Habit, HabitCompletion, HabitWithStats
├── data/
│   ├── local/          # Room DB, DAOs, Entities
│   └── repository/     # HabitRepository — single source of truth
├── presentation/
│   ├── ui/home/        # Home screen + ViewModel
│   ├── ui/addhabit/    # Add/Edit screen + ViewModel
│   ├── ui/statistics/  # Statistics screen + ViewModel
│   ├── ui/settings/    # Settings screen
│   ├── components/     # Reusable HabitCard composable
│   └── theme/          # Material 3 color, type, theme
├── di/                 # Hilt AppModule
├── util/               # DateUtils, NotificationScheduler, ThemePreferences
└── worker/             # NotificationReceiver, BootReceiver
```

The UI never talks to the database directly — all data flows through the repository as a `Flow`, so the UI reacts automatically to any change (toggle, add, delete) without manual refresh.

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1) or newer
- JDK 17+
- Android SDK 26+

### Installation

1. Clone the repository
   ```bash
   git clone https://github.com/Eshan549/HabitTracker.git
   ```

2. Open in Android Studio
   ```
   File > Open > select the HabitTracker folder
   ```

3. Let Gradle sync finish (first sync downloads dependencies — takes 2–5 min)

4. Run on emulator or physical device
   ```
   Run > Run 'app'  (Shift + F10)
   ```

---

## Dependencies

```toml
# Core
androidx-compose-bom       = "2024.09.03"
androidx-navigation        = "2.8.2"
androidx-lifecycle         = "2.8.6"

# Database
room                       = "2.6.1"

# Dependency Injection
hilt                       = "2.52"

# Background / Notifications
work-runtime               = "2.9.1"

# Preferences
datastore-preferences      = "1.1.1"
```

---

## Key Implementation Details

**Reactive toggle fix** — Habit completion state uses `combine(habitsFlow, completionsFlow)` so tapping the checkmark instantly updates the UI without any manual refresh or additional DB query.

**Streak calculation** — Computed on the fly from sorted completion dates, respecting each habit's target days (e.g. a Mon–Fri habit won't break streak on weekends).

**Theme persistence** — Theme mode (Light / Dark / System) stored in DataStore and read at app startup before the first frame renders, preventing flash of wrong theme.

---

## License

```
MIT License — feel free to use, modify, and distribute.
```
