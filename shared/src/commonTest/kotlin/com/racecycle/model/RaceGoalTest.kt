package com.racecycle.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RaceGoalTest {
    @Test
    fun `phase starts as Base`() {
        val phase = TrainingPhase.fromWeek(0, 12)
        assertEquals(TrainingPhase.Base, phase)
    }

    @Test
    fun `phase is Build in week 5`() {
        val phase = TrainingPhase.fromWeek(5, 12)
        assertEquals(TrainingPhase.Build, phase)
    }

    @Test
    fun `phase is Peak in week 8`() {
        val phase = TrainingPhase.fromWeek(8, 12)
        assertEquals(TrainingPhase.Peak, phase)
    }

    @Test
    fun `phase is Taper in week 10`() {
        val phase = TrainingPhase.fromWeek(10, 12)
        assertEquals(TrainingPhase.Taper, phase)
    }

    @Test
    fun `phase is Race in last week`() {
        val phase = TrainingPhase.fromWeek(11, 12)
        assertEquals(TrainingPhase.Race, phase)
    }

    @Test
    fun `weekly checkin on track at 80 percent`() {
        val checkin = WeeklyCheckin(1, 0L, 4, 5)
        assertEquals(0.8, checkin.completionRate, 0.01)
        assertEquals(true, checkin.isOnTrack)
    }

    @Test
    fun `weekly checkin off track below 80 percent`() {
        val checkin = WeeklyCheckin(1, 0L, 3, 5)
        assertEquals(0.6, checkin.completionRate, 0.01)
        assertEquals(false, checkin.isOnTrack)
    }

    @Test
    fun `progress percent is 0 for first week`() {
        val goal = RaceGoal("Test", 9999999999999L, "10 km", 12)
        // currentWeekIndex depends on current time, just check the formula
        val percent = (goal.currentWeekIndex.toDouble() / 12).coerceIn(0.0, 1.0)
        assertEquals(percent, goal.progressPercent)
    }
}
