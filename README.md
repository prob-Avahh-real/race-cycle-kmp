# race-cycle-kmp
A racing/training cycle tracker built with Kotlin Multiplatform + Compose Multiplatform.

## Structure
- `shared/` — shared models: RaceGoal, TrainingPhase, WeeklyCheckin
- `composeApp/` — Compose Multiplatform dashboard UI
- `iosApp/` — iOS entry point

## Build
```bash
./gradlew :composeApp:assembleDebug
```

## Features
- Marathon training phase tracking (Base/Build/Peak/Taper/Race)
- Weekly check-in system with RPE tracking
- Progress dashboard with phase cards
