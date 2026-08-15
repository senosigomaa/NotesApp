package com.senosi.notes.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senosi.notes.data.Note
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextMuted
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale
import kotlin.math.max

private enum class StatsRange(
    val label: String
) {
    DAYS_7("7 Days"),
    DAYS_30("30 Days"),
    MONTHS_3("3 Months"),
    YEAR_1("1 Year"),
    ALL("All")
}

@Composable
fun StatisticsScreen(
    notes: List<Note>
) {
    var selectedRange by remember {
        mutableStateOf(
            StatsRange.DAYS_30
        )
    }

    val today = remember {
        LocalDate.now()
    }

    val zoneId = remember {
        ZoneId.systemDefault()
    }

    val startDate = remember(
        selectedRange,
        notes
    ) {

        when (selectedRange) {

            StatsRange.DAYS_7 ->
                today.minusDays(6)

            StatsRange.DAYS_30 ->
                today.minusDays(29)

            StatsRange.MONTHS_3 ->
                today.minusMonths(3)

            StatsRange.YEAR_1 ->
                today.minusYears(1)

            StatsRange.ALL ->
                notes
                    .map {
                        noteDate(
                            it,
                            zoneId
                        )
                    }
                    .minOrNull()
                    ?: today
        }
    }

    val activeNotes =
        notes.filter {
            !it.isDeleted
        }

    val deletedNotes =
        notes.filter {
            it.isDeleted
        }

    val favoriteNotes =
        activeNotes.filter {
            it.isFavorite
        }

    val periodNotes =
        activeNotes.filter {

            val date = noteDate(
                it,
                zoneId
            )

            !date.isBefore(startDate) &&
                    !date.isAfter(today)
        }

    val totalCount =
        activeNotes.size

    val activeCount =
        activeNotes.size

    val favoriteCount =
        favoriteNotes.size

    val deletedCount =
        deletedNotes.size

    val activityScore =
        calculateActivityScore(
            activeCount = activeCount,
            favoriteCount = favoriteCount,
            periodCount = periodNotes.size
        )

    val notesByColor =
        activeNotes.groupingBy {
            colorName(it.color)
        }.eachCount()

    val activityByDate =
        buildActivityMap(
            periodNotes,
            startDate,
            today,
            zoneId
        )

    val bestDay =
        periodNotes
            .groupBy {
                noteDate(
                    it,
                    zoneId
                ).dayOfWeek
            }
            .maxByOrNull {
                it.value.size
            }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),

        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 18.dp,
            bottom = 30.dp
        ),

        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        item {

            StatisticsHeader()
        }

        item {

            StatsRangeSelector(
                selectedRange = selectedRange,
                onRangeSelected = {
                    selectedRange = it
                }
            )
        }

        item {

            StatsOverviewGrid(
                total = totalCount,
                active = activeCount,
                favorites = favoriteCount,
                deleted = deletedCount
            )
        }

        item {

            ActivityChartCard(
                values =
                    activityByDate.values.toList()
            )
        }

        item {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                NotesByColorCard(
                    data = notesByColor,
                    modifier =
                        Modifier.weight(1f)
                )

                ActivityScoreCard(
                    score = activityScore,
                    modifier =
                        Modifier.weight(1f)
                )
            }
        }

        item {

            BestDayCard(
                bestDay = bestDay?.key?.name,
                count = bestDay?.value?.size ?: 0
            )
        }

        item {

            StatisticsFooter()
        }
    }
}

// ============================================================================
// HEADER
// ============================================================================

@Composable
private fun StatisticsHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 8.dp
            ),
        verticalAlignment =
            Alignment.Top
    ) {

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text = "Statistics",
                color = TextPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text =
                    "Your notes, insights, in numbers.",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(
                    RoundedCornerShape(18.dp)
                )
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Primary.copy(alpha = 0.22f),
                            Color(0xFFFF4FD8)
                                .copy(alpha = 0.08f)
                        )
                    )
                )
                .border(
                    1.dp,
                    Primary.copy(alpha = 0.28f),
                    RoundedCornerShape(18.dp)
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    Icons.Default.BarChart,
                contentDescription = null,
                tint = Primary,
                modifier =
                    Modifier.size(28.dp)
            )
        }
    }
}

// ============================================================================
// RANGE
// ============================================================================

@Composable
private fun StatsRangeSelector(
    selectedRange: StatsRange,
    onRangeSelected: (StatsRange) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(Surface)
            .border(
                1.dp,
                SurfaceLight,
                RoundedCornerShape(22.dp)
            )
            .padding(5.dp),
        horizontalArrangement =
            Arrangement.spacedBy(3.dp)
    ) {

        StatsRange.entries.forEach { range ->

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(18.dp)
                    )
                    .background(
                        if (
                            range ==
                            selectedRange
                        ) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Primary,
                                    Color(0xFFB84DFF)
                                )
                            )
                        } else {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            )
                        }
                    )
                    .padding(
                        vertical = 9.dp
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = range.label,
                    color =
                        if (
                            range ==
                            selectedRange
                        ) {
                            Color.White
                        } else {
                            TextSecondary
                        },
                    fontSize = 10.sp,
                    fontWeight =
                        if (
                            range ==
                            selectedRange
                        ) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                )
            }
        }
    }
}

// ============================================================================
// OVERVIEW
// ============================================================================

@Composable
private fun StatsOverviewGrid(
    total: Int,
    active: Int,
    favorites: Int,
    deleted: Int
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            StatCard(
                title = "Total Notes",
                value = total,
                icon = Icons.Default.Edit,
                accent = Primary,
                modifier =
                    Modifier.weight(1f)
            )

            StatCard(
                title = "Active",
                value = active,
                icon = Icons.Default.TrendingUp,
                accent = Color(0xFFFF4FD8),
                modifier =
                    Modifier.weight(1f)
            )
        }

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            StatCard(
                title = "Favorites",
                value = favorites,
                icon = Icons.Default.Favorite,
                accent = Color(0xFF00E5B8),
                modifier =
                    Modifier.weight(1f)
            )

            StatCard(
                title = "Deleted",
                value = deleted,
                icon = Icons.Default.DeleteOutline,
                accent = Color(0xFFFFB800),
                modifier =
                    Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: Int,
    icon: ImageVector,
    accent: Color,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Surface,
                        SurfaceLight.copy(
                            alpha = 0.30f
                        )
                    )
                )
            )
            .border(
                1.dp,
                SurfaceLight,
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(
                    RoundedCornerShape(14.dp)
                )
                .background(
                    accent.copy(alpha = 0.10f)
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier =
                    Modifier.size(22.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = value.toString(),
            color = TextPrimary,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = title,
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

// ============================================================================
// ACTIVITY CHART
// ============================================================================

@Composable
private fun ActivityChartCard(
    values: List<Int>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(Surface)
            .border(
                1.dp,
                SurfaceLight,
                RoundedCornerShape(22.dp)
            )
            .padding(17.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = "Notes Over Time",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        "Updates based on note activity",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(14.dp)
                    )
                    .background(
                        Primary.copy(alpha = 0.08f)
                    )
                    .border(
                        1.dp,
                        Primary.copy(alpha = 0.22f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(
                        horizontal = 11.dp,
                        vertical = 7.dp
                    )
            ) {

                Text(
                    text = "Daily",
                    color = Primary,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        ActivityLineChart(
            values = values
        )
    }
}

@Composable
private fun ActivityLineChart(
    values: List<Int>
) {

    val safeValues =
        if (values.isEmpty()) {
            listOf(0)
        } else {
            values
        }

    val maxValue =
        max(
            1,
            safeValues.maxOrNull() ?: 1
        )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {

        val chartWidth =
            size.width

        val chartHeight =
            size.height

        repeat(4) { index ->

            val y =
                chartHeight *
                        index / 3f

            drawLine(
                color =
                    SurfaceLight.copy(
                        alpha = 0.55f
                    ),
                start = Offset(
                    0f,
                    y
                ),
                end = Offset(
                    chartWidth,
                    y
                ),
                strokeWidth = 1f
            )
        }

        val points =
            safeValues.mapIndexed { index, value ->

                val x =
                    if (safeValues.size == 1) {
                        chartWidth / 2f
                    } else {
                        index.toFloat() /
                                (safeValues.lastIndex)
                                    .coerceAtLeast(1) *
                         chartWidth
                    }

                val y =
                    chartHeight -
                            (
                                    value.toFloat() /
                                            maxValue
                                    ) *
                            (chartHeight - 20f)

                Offset(
                    x,
                    y
                )
            }

        val linePath =
            Path()

        points.forEachIndexed {
                index,
                point ->

            if (index == 0) {
                linePath.moveTo(
                    point.x,
                    point.y
                )
            } else {
                linePath.lineTo(
                    point.x,
                    point.y
                )
            }
        }

        drawPath(
            path = linePath,
            color = Primary,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )

        points.forEach { point ->

            drawCircle(
                color =
                    Primary.copy(
                        alpha = 0.18f
                    ),
                radius = 9f,
                center = point
            )

            drawCircle(
                color = Primary,
                radius = 4f,
                center = point
            )
        }
    }
}

// ============================================================================
// COLOR BREAKDOWN
// ============================================================================

@Composable
private fun NotesByColorCard(
    data: Map<String, Int>,
    modifier: Modifier
) {

    val total =
        data.values.sum()
            .coerceAtLeast(1)

    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(Surface)
            .border(
                1.dp,
                SurfaceLight,
                RoundedCornerShape(22.dp)
            )
            .padding(15.dp)
    ) {

        Text(
            text = "Notes by Color",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        data.entries
            .sortedByDescending {
                it.value
            }
            .take(5)
            .forEach { entry ->

                ColorRow(
                    name = entry.key,
                    count = entry.value,
                    percentage =
                        entry.value * 100 / total
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )
            }

        if (data.isEmpty()) {

            Text(
                text = "No active notes",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ColorRow(
    name: String,
    count: Int,
    percentage: Int
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                    colorForName(name)
                )
        )

        Spacer(
            modifier = Modifier.width(7.dp)
        )

        Text(
            text = name,
            color = TextSecondary,
            fontSize = 10.sp,
            modifier =
                Modifier.weight(1f)
        )

        Text(
            text = "$count ($percentage%)",
            color = TextPrimary,
            fontSize = 10.sp
        )
    }
}

// ============================================================================
// ACTIVITY SCORE
// ============================================================================

@Composable
private fun ActivityScoreCard(
    score: Int,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(Surface)
            .border(
                1.dp,
                SurfaceLight,
                RoundedCornerShape(22.dp)
            )
            .padding(15.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "Activity Score",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment =
                Alignment.Center
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                val stroke =
                    13.dp.toPx()

                drawArc(
                    color =
                        SurfaceLight,
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style =
                        Stroke(
                            width = stroke,
                            cap =
                                StrokeCap.Round
                        )
                )

                drawArc(
                    color = Primary,
                    startAngle = 135f,
                    sweepAngle =
                        270f *
                                score /
                                100f,
                    useCenter = false,
                    style =
                        Stroke(
                            width = stroke,
                            cap =
                                StrokeCap.Round
                        )
                )
            }

            Text(
                text = "$score%",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text =
                when {
                    score >= 80 ->
                        "Excellent!"

                    score >= 60 ->
                        "Great progress"

                    score >= 40 ->
                        "Keep going"

                    else ->
                        "Let's build momentum"
                },
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

// ============================================================================
// BEST DAY
// ============================================================================

@Composable
private fun BestDayCard(
    bestDay: String?,
    count: Int
) {

    val day =
        bestDay
            ?.lowercase()
            ?.replaceFirstChar {
                it.uppercase()
            }
            ?: "No data"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Surface,
                        Primary.copy(alpha = 0.08f)
                    )
                )
            )
            .border(
                1.dp,
                SurfaceLight,
                RoundedCornerShape(22.dp)
            )
            .padding(16.dp),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(
                    RoundedCornerShape(17.dp)
                )
                .background(
                    Primary.copy(alpha = 0.12f)
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Icon(
                imageVector =
                    Icons.Default.Star,
                contentDescription = null,
                tint = Primary,
                modifier =
                    Modifier.size(29.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text = "Best Day",
                color = TextSecondary,
                fontSize = 11.sp
            )

            Text(
                text = day,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text =
                    if (count > 0) {
                        "$count note updates"
                    } else {
                        "No activity yet"
                    },
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        Text(
            text = "Activity",
            color = Primary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ============================================================================
// FOOTER
// ============================================================================

@Composable
private fun StatisticsFooter() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 4.dp,
                bottom = 10.dp
            ),
        horizontalArrangement =
            Arrangement.Center
    ) {

        Icon(
            imageVector =
                Icons.Default.TrendingUp,
            contentDescription = null,
            tint =
                Primary.copy(alpha = 0.75f),
            modifier =
                Modifier.size(15.dp)
        )

        Spacer(
            modifier = Modifier.width(6.dp)
        )

        Text(
            text =
                "Statistics are calculated from your real notes.",
            color = TextMuted,
            fontSize = 10.sp
        )
    }
}

// ============================================================================
// HELPERS
// ============================================================================

private fun noteDate(
    note: Note,
    zoneId: ZoneId
): LocalDate {

    return Instant
        .ofEpochMilli(note.updatedAt)
        .atZone(zoneId)
        .toLocalDate()
}

private fun buildActivityMap(
    notes: List<Note>,
    startDate: LocalDate,
    endDate: LocalDate,
    zoneId: ZoneId
): Map<LocalDate, Int> {

    val result =
        linkedMapOf<LocalDate, Int>()

    var date = startDate

    while (!date.isAfter(endDate)) {

        result[date] = 0

        date = date.plusDays(1)
    }

    notes.forEach { note ->

        val dateValue =
            noteDate(
                note,
                zoneId
            )

        if (result.containsKey(dateValue)) {

            result[dateValue] =
                (result[dateValue] ?: 0) + 1
        }
    }

    return result
}

private fun calculateActivityScore(
    activeCount: Int,
    favoriteCount: Int,
    periodCount: Int
): Int {

    if (activeCount == 0) {
        return 0
    }

    val activeScore = 60

    val favoriteScore =
        (
                favoriteCount.toFloat() /
                        activeCount
                        * 20f
                ).toInt()

    val activityScore =
        (
                periodCount
                    .coerceAtMost(20)
                    .toFloat() /
                        20f *
                        20f
                ).toInt()

    return (
            activeScore +
                    favoriteScore +
                    activityScore
            ).coerceIn(0, 100)
}

private fun colorName(
    value: String
): String {

    return when (
        value.uppercase(Locale.US)
    ) {

        "#7C5CFC",
        "#6C5CE7" ->
            "Purple"

        "#FDCB6E" ->
            "Yellow"

        "#55EFC4" ->
            "Mint"

        "#FF7675" ->
            "Red"

        else ->
            "Other"
    }
}

private fun colorForName(
    name: String
): Color {

    return when (name) {

        "Purple" ->
            Primary

        "Yellow" ->
            Color(0xFFFFB800)

        "Mint" ->
            Color(0xFF00E5B8)

        "Red" ->
            Color(0xFFFF4D6D)

        else ->
            Color(0xFF636778)
    }
}