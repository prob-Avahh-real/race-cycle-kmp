// RaceGoal.kt — marathon training phase model
package com.racecycle.model

import kotlin.math.ceil

enum class TrainingPhase(val label: String, val description: String) {
    Base("基础期", "建立有氧基础，适应训练节奏"),
    Build("强化期", "提升配速，增加长距离里程"),
    Peak("巅峰期", "强度最大，模拟比赛节奏"),
    Taper("减量期", "降低训练量，储备体能"),
    Race("比赛周", "轻量活动，调整状态");

    companion object {
        fun fromWeek(weekIndex: Int, totalWeeks: Int): TrainingPhase = when {
            weekIndex >= totalWeeks - 1 -> Race
            weekIndex >= totalWeeks - 3 -> Taper
            weekIndex >= totalWeeks / 2 + 2 -> Peak
            weekIndex >= 4 -> Build
            else -> Base
        }
    }
}

data class RaceGoal(
    val raceName: String,
    val raceDate: Long, // epoch millis
    val distance: String,
    val totalWeeks: Int
) {
    private fun nowMs(): Long = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()

    val weeksRemaining: Int get() {
        val diff = (raceDate - nowMs()) / (24 * 60 * 60 * 1000)
        return ceil(diff.toDouble() / 7.0).toInt().coerceIn(0, totalWeeks)
    }

    val currentWeekIndex: Int get() = totalWeeks - weeksRemaining
    val currentPhase: TrainingPhase get() = TrainingPhase.fromWeek(currentWeekIndex, totalWeeks)
    val progressPercent: Double get() =
        (currentWeekIndex.toDouble() / totalWeeks).coerceIn(0.0, 1.0)

    val daysLeft: Int get() {
        val diff = (raceDate - nowMs()) / (24 * 60 * 60 * 1000)
        return diff.toInt().coerceAtLeast(0)
    }
}

enum class BodyFeeling(val label: String) {
    Great("状态极佳"),
    Good("感觉不错"),
    Normal("中规中矩"),
    Tired("有些疲惫"),
    Injured("有伤在身")
}

data class WeeklyCheckin(
    val weekNumber: Int,
    val date: Long,
    val completedRuns: Int,
    val plannedRuns: Int,
    val rpe: Int? = null,
    val note: String? = null,
    val feeling: BodyFeeling? = null
) {
    val completionRate: Double get() =
        if (plannedRuns > 0) completedRuns.toDouble() / plannedRuns else 0.0
    val isOnTrack: Boolean get() = completionRate >= 0.8
}
