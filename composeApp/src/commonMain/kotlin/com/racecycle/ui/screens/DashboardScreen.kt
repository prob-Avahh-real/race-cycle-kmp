// DashboardScreen.kt — marathon training dashboard
package com.racecycle.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.racecycle.model.*

@Composable
fun DashboardScreen(
    goal: RaceGoal,
    checkins: List<WeeklyCheckin>,
    onCheckin: () -> Unit,
    onReset: () -> Unit
) {
    val phase = goal.currentPhase
    val week = goal.currentWeekIndex + 1
    val remaining = goal.weeksRemaining
    val daysLeft = goal.daysLeft

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("赛季仪表盘") },
                actions = {
                    IconButton(onClick = onReset) {
                        Text("↻", fontSize = 20.sp, color = Color.White)
                    }
                })
        }
    ) {
        Column(
            Modifier.padding(it).padding(20.dp).verticalScroll(rememberScrollState())
        ) {
            // Race card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(goal.raceName,
                        color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Chip(goal.distance)
                        Chip("${goal.totalWeeks}周计划")
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("距离比赛", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                            Text("$daysLeft 天", color = Color.White,
                                fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("🚩", fontSize = 40.sp)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Phase card
            PhaseCard(phase)

            Spacer(Modifier.height(20.dp))

            // Progress bar
            Text("赛季进度", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { goal.progressPercent.toFloat() },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(6.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.Gray.copy(alpha = 0.2f)
            )
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("第 $week 周", color = Color.Gray, fontSize = 12.sp)
                Text("还剩 $remaining 周", color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(Modifier.height(28.dp))

            // Checkin header
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text("赛季打卡", style = MaterialTheme.typography.titleMedium)
                OutlinedButton(onClick = onCheckin) { Text("本周打卡") }
            }

            if (checkins.isEmpty()) {
                Surface(Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = Color.Gray.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📋", fontSize = 36.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("还没有打卡记录", color = Color.Gray)
                        Text("完成第一次周检查，开始追踪你的赛季",
                            color = Color.Gray.copy(alpha = 0.6f), fontSize = 13.sp)
                    }
                }
            } else {
                checkins.reversed().take(8).forEach { checkin ->
                    CheckinTile(checkin)
                }
            }
        }
    }
}

@Composable
fun Chip(text: String) {
    Surface(
        color = Color.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, color = Color.White, fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}

@Composable
fun PhaseCard(phase: TrainingPhase) {
    val icon = when (phase) {
        TrainingPhase.Base -> "🏗"
        TrainingPhase.Build -> "📈"
        TrainingPhase.Peak -> "🔥"
        TrainingPhase.Taper -> "🧘"
        TrainingPhase.Race -> "🏆"
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFFF3E0),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFFFB74D))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(48.dp),
                color = Color(0xFFFFE0B2),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 24.sp)
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("当前阶段：${phase.label}",
                    fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                Text(phase.description, color = Color(0xFF616161), fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun CheckinTile(checkin: WeeklyCheckin) {
    Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            val bgColor = if (checkin.isOnTrack) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
            val icon = if (checkin.isOnTrack) "✅" else "⚠️"
            Surface(
                modifier = Modifier.size(40.dp),
                color = bgColor,
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 18.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("第 ${checkin.weekNumber} 周", fontWeight = FontWeight.SemiBold)
                Text("${checkin.completedRuns}/${checkin.plannedRuns} 次训练" +
                    checkin.feeling?.let { " · ${it.label}" }.orEmpty(),
                    color = Color.Gray, fontSize = 13.sp)
            }
            checkin.rpe?.let {
                Text("RPE $it", color = Color.DarkGray, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
