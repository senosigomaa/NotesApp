package com.senosi.notes.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senosi.notes.data.CalendarEvent
import com.senosi.notes.ui.theme.Background
import com.senosi.notes.ui.theme.Primary
import com.senosi.notes.ui.theme.Surface
import com.senosi.notes.ui.theme.SurfaceLight
import com.senosi.notes.ui.theme.TextMuted
import com.senosi.notes.ui.theme.TextPrimary
import com.senosi.notes.ui.theme.TextSecondary
import com.senosi.notes.viewmodel.CalendarViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    onBackClick: () -> Unit = {},
    onAddEventClick: () -> Unit = {},
    viewModel: CalendarViewModel = viewModel()
) {
    val today = remember {
        LocalDate.now()
    }

    var selectedDate by remember {
        mutableStateOf(today)
    }

    var displayedMonth by remember {
        mutableStateOf(
            YearMonth.from(today)
        )
    }

    val selectedDateString = selectedDate.toString()

    val events by viewModel
        .observeEventsForDate(selectedDateString)
        .collectAsState(initial = emptyList())

    val upcomingEvents by viewModel
        .observeUpcomingEvents(
            displayedMonth
                .atDay(1)
                .toString()
        )
        .collectAsState(initial = emptyList())

    val eventsByDate = remember(upcomingEvents) {

        upcomingEvents.groupBy {

            runCatching {
                LocalDate.parse(it.eventDate)
            }.getOrNull()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),

        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 18.dp,
            bottom = 110.dp
        ),

        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        // ============================================================
        // HEADER
        // ============================================================

        item {

            CalendarHeader(
                onBackClick = onBackClick,
                onAddClick = onAddEventClick
            )
        }

        item {

            Spacer(
                modifier = Modifier.height(22.dp)
            )
        }

        // ============================================================
        // CALENDAR
        // ============================================================

        item {

            CalendarCard(
                month = displayedMonth,
                selectedDate = selectedDate,
                today = today,
                eventsByDate = eventsByDate,

                onPreviousMonth = {
                    displayedMonth =
                        displayedMonth.minusMonths(1)
                },

                onNextMonth = {
                    displayedMonth =
                        displayedMonth.plusMonths(1)
                },

                onDateSelected = {

                    selectedDate = it

                    displayedMonth =
                        YearMonth.from(it)
                }
            )
        }

        item {

            Spacer(
                modifier = Modifier.height(22.dp)
            )
        }

        // ============================================================
        // SELECTED DATE
        // ============================================================

        item {

            SelectedDateHeader(
                selectedDate = selectedDate,
                today = today,

                onTodayClick = {

                    selectedDate = today

                    displayedMonth =
                        YearMonth.from(today)
                }
            )
        }

        item {

            Spacer(
                modifier = Modifier.height(14.dp)
            )
        }

        // ============================================================
        // DAILY EVENTS
        // ============================================================

        if (events.isEmpty()) {

            item {
                EmptyAgenda()
            }

        } else {

            items(
                count = events.size,

                key = { index ->
                    events[index].id
                }

            ) { index ->

                EventCard(
                    event = events[index]
                )

                if (index != events.lastIndex) {

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }
            }
        }

        item {

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }

        // ============================================================
        // UPCOMING
        // ============================================================

        item {
            UpcomingHeader()
        }

        item {

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        val upcoming = upcomingEvents
            .filter {

                val date = runCatching {
                    LocalDate.parse(it.eventDate)
                }.getOrNull()

                date != null &&
                        date.isAfter(selectedDate)
            }
            .take(5)

        if (upcoming.isEmpty()) {

            item {
                EmptyUpcoming()
            }

        } else {

            items(
                count = upcoming.size,

                key = { index ->
                    "upcoming_${upcoming[index].id}"
                }

            ) { index ->

                UpcomingEventCard(
                    event = upcoming[index]
                )

                if (index != upcoming.lastIndex) {

                    Spacer(
                        modifier = Modifier.height(1.dp)
                    )
                }
            }
        }

        item {

            Spacer(
                modifier = Modifier.height(28.dp)
            )
        }

        item {
            DecorativeWave()
        }
    }
}

// ============================================================================
// HEADER
// ============================================================================

// ============================================================================
// PROFESSIONAL CALENDAR HEADER
// ============================================================================

@Composable
private fun CalendarHeader(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(
                top = 8.dp,
                bottom = 2.dp
            )
            .clip(
                RoundedCornerShape(30.dp)
            )
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Surface.copy(alpha = 0.96f),
                        SurfaceLight.copy(alpha = 0.72f),
                        Surface.copy(alpha = 0.96f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Primary.copy(alpha = 0.28f),
                shape = RoundedCornerShape(30.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 9.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    SurfaceLight.copy(alpha = 0.55f)
                )
                .border(
                    width = 1.dp,
                    color = Primary.copy(alpha = 0.22f),
                    shape = CircleShape
                )
                .clickable(
                    onClick = onBackClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Primary,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Calendar",
                color = TextPrimary,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = "Plan your days, stay on track",
                color = TextSecondary,
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    SurfaceLight.copy(alpha = 0.55f)
                )
                .border(
                    width = 1.dp,
                    color = SurfaceLight,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Calendar",
                tint = Primary,
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Primary,
                            Color(0xFF9D4DFF)
                        )
                    )
                )
                .clickable(
                    onClick = onAddClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add event",
                tint = Color.White,
                modifier = Modifier.size(29.dp)
            )
        }
    }
}
// ============================================================================
// CALENDAR CARD
// ============================================================================

@Composable
private fun CalendarCard(
    month: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    eventsByDate: Map<LocalDate?, List<CalendarEvent>>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(22.dp)
            )
            .background(Surface)
            .border(
                width = 1.dp,
                color = SurfaceLight,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = month.format(
                    DateTimeFormatter.ofPattern(
                        "MMMM yyyy",
                        Locale.US
                    )
                ),

                color = TextPrimary,

                fontSize = 21.sp,

                fontWeight = FontWeight.Bold,

                modifier = Modifier.weight(1f)
            )

            CalendarArrow(
                icon =
                    Icons.Default.ArrowBackIosNew,

                onClick =
                    onPreviousMonth
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            CalendarArrow(
                icon =
                    Icons.Default.ArrowForwardIos,

                onClick =
                    onNextMonth
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        WeekDays()

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        val firstDay =
            month.atDay(1)

        val daysBefore =
            firstDay
                .dayOfWeek
                .value % 7

        val calendarStart =
            firstDay.minusDays(
                daysBefore.toLong()
            )

        for (week in 0 until 6) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                for (dayIndex in 0 until 7) {

                    val date =
                        calendarStart.plusDays(
                            (week * 7L) +
                                    dayIndex
                        )

                    CalendarDay(
                        date = date,
                        month = month,
                        selectedDate = selectedDate,
                        today = today,

                        eventColors =
                            eventsByDate[date]
                                ?.mapNotNull {
                                    parseColor(it.color)
                                }
                                ?.take(3)
                                ?: emptyList(),

                        onClick = {
                            onDateSelected(date)
                        },

                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }

            if (week != 5) {

                Spacer(
                    modifier = Modifier.height(5.dp)
                )
            }
        }
    }
}

// ============================================================================
// WEEK DAYS
// ============================================================================

@Composable
private fun WeekDays() {

    val days = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        days.forEach { day ->

            Text(
                text = day.getDisplayName(
                    TextStyle.SHORT,
                    Locale.US
                ),

                color = TextSecondary,

                fontSize = 12.sp,

                fontWeight = FontWeight.Medium,

                modifier = Modifier.weight(1f),

                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================================
// CALENDAR DAY
// ============================================================================

@Composable
private fun CalendarDay(
    date: LocalDate,
    month: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    eventColors: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier
) {

    val isCurrentMonth =
        YearMonth.from(date) == month

    val isSelected =
        date == selectedDate

    val isToday =
        date == today

    Box(
        modifier = modifier
            .height(52.dp)
            .clickable(
                onClick = onClick
            ),

        contentAlignment = Alignment.Center
    ) {

        if (isSelected) {

            Box(
                modifier = Modifier
                    .size(43.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Primary,
                                Color(0xFF9D4DFF)
                            )
                        )
                    ),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text =
                        date.dayOfMonth.toString(),

                    color = Color.White,

                    fontSize = 16.sp,

                    fontWeight = FontWeight.Bold
                )
            }

        } else {

            Text(
                text =
                    date.dayOfMonth.toString(),

                color = when {

                    !isCurrentMonth ->
                        TextMuted.copy(
                            alpha = 0.55f
                        )

                    isToday ->
                        Primary

                    else ->
                        TextPrimary
                },

                fontSize = 16.sp,

                fontWeight =
                    if (isToday) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    }
            )
        }

        if (eventColors.isNotEmpty()) {

            Row(
                modifier = Modifier
                    .align(
                        Alignment.BottomCenter
                    )
                    .padding(
                        bottom = 2.dp
                    ),

                horizontalArrangement =
                    Arrangement.spacedBy(2.dp)
            ) {

                eventColors.forEach { color ->

                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
        }
    }
}

// ============================================================================
// CALENDAR ARROW
// ============================================================================

@Composable
private fun CalendarArrow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                SurfaceLight.copy(
                    alpha = 0.65f
                )
            )
            .clickable(
                onClick = onClick
            ),

        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,

            contentDescription = null,

            tint = TextPrimary,

            modifier = Modifier.size(15.dp)
        )
    }
}

// ============================================================================
// SELECTED DATE HEADER
// ============================================================================

@Composable
private fun SelectedDateHeader(
    selectedDate: LocalDate,
    today: LocalDate,
    onTodayClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        val dateText = when {

            selectedDate == today -> {

                "Today • ${
                    selectedDate.format(
                        DateTimeFormatter.ofPattern(
                            "MMM dd, yyyy",
                            Locale.US
                        )
                    )
                }"
            }

            else -> {

                selectedDate.format(
                    DateTimeFormatter.ofPattern(
                        "EEE • MMM dd, yyyy",
                        Locale.US
                    )
                )
            }
        }

        Text(
            text = dateText,

            color = Primary,

            fontSize = 18.sp,

            fontWeight =
                FontWeight.SemiBold,

            modifier =
                Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(50)
                )
                .background(
                    Primary.copy(
                        alpha = 0.08f
                    )
                )
                .border(
                    width = 1.dp,

                    color = Primary.copy(
                        alpha = 0.25f
                    ),

                    shape =
                        RoundedCornerShape(50)
                )
                .clickable(
                    onClick = onTodayClick
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {

            Text(
                text = "Today",

                color = Primary,

                fontSize = 12.sp,

                fontWeight =
                    FontWeight.Medium
            )
        }
    }
}

// ============================================================================
// EVENT CARD
// ============================================================================

@Composable
private fun EventCard(
    event: CalendarEvent
) {

    val color =
        parseColor(event.color)
            ?: Primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Surface,
                        SurfaceLight.copy(
                            alpha = 0.42f
                        )
                    )
                )
            )
            .border(
                width = 1.dp,
                color = SurfaceLight,
                shape =
                    RoundedCornerShape(18.dp)
            )
    ) {

        Box(
            modifier = Modifier
                .width(4.dp)
                .height(112.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            color,
                            color.copy(
                                alpha = 0.25f
                            )
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .background(
                        color.copy(
                            alpha = 0.12f
                        )
                    )
                    .border(
                        width = 1.dp,

                        color = color.copy(
                            alpha = 0.18f
                        ),

                        shape =
                            RoundedCornerShape(15.dp)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.CalendarMonth,

                    contentDescription = null,

                    tint = color,

                    modifier =
                        Modifier.size(25.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(13.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = event.title,

                    color = TextPrimary,

                    fontSize = 16.sp,

                    fontWeight =
                        FontWeight.SemiBold,

                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text =
                        event.description.ifBlank {
                            "No description"
                        },

                    color = TextSecondary,

                    fontSize = 13.sp,

                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(color)
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = event.category,

                        color = TextSecondary,

                        fontSize = 12.sp
                    )
                }
            }

            Column(
                horizontalAlignment =
                    Alignment.End
            ) {

                Text(
                    text = event.eventTime,

                    color = color,

                    fontSize = 13.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Icon(
                    imageVector =
                        if (event.isReminderEnabled) {
                            Icons.Default.NotificationsNone
                        } else {
                            Icons.Default.Today
                        },

                    contentDescription = null,

                    tint = TextSecondary,

                    modifier =
                        Modifier.size(20.dp)
                )
            }
        }
    }
}

// ============================================================================
// UPCOMING HEADER
// ============================================================================

@Composable
private fun UpcomingHeader() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = "Upcoming",

            color = Primary,

            fontSize = 19.sp,

            fontWeight =
                FontWeight.SemiBold,

            modifier =
                Modifier.weight(1f)
        )

        Text(
            text = "View All",

            color = Primary,

            fontSize = 13.sp,

            fontWeight =
                FontWeight.Medium
        )
    }
}

// ============================================================================
// UPCOMING EVENT
// ============================================================================

@Composable
private fun UpcomingEventCard(
    event: CalendarEvent
) {

    val color =
        parseColor(event.color)
            ?: Primary

    val date =
        runCatching {
            LocalDate.parse(
                event.eventDate
            )
        }.getOrNull()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .border(
                width = 1.dp,
                color = SurfaceLight,
                shape =
                    RoundedCornerShape(0.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 16.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        if (date != null) {

            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally,

                modifier =
                    Modifier.width(58.dp)
            ) {

                Text(
                    text = date.month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.US
                    ),

                    color = TextSecondary,

                    fontSize = 12.sp
                )

                Text(
                    text =
                        date.dayOfMonth.toString(),

                    color = TextPrimary,

                    fontSize = 23.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT,
                            Locale.US
                        ),

                    color = TextSecondary,

                    fontSize = 11.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Box(
            modifier = Modifier
                .width(3.dp)
                .height(58.dp)
                .clip(
                    RoundedCornerShape(50)
                )
                .background(color)
        )

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = event.title,

                color = TextPrimary,

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = event.description,

                color = TextSecondary,

                fontSize = 13.sp,

                maxLines = 1
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(color)
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text =
                        "${event.category} • ${event.eventTime}",

                    color = TextSecondary,

                    fontSize = 11.sp
                )
            }
        }

        Icon(
            imageVector =
                Icons.Default.MoreVert,

            contentDescription = "More",

            tint = TextMuted,

            modifier =
                Modifier.size(22.dp)
        )
    }
}

// ============================================================================
// EMPTY AGENDA
// ============================================================================

@Composable
private fun EmptyAgenda() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(Surface)
            .border(
                width = 1.dp,
                color = SurfaceLight,
                shape =
                    RoundedCornerShape(18.dp)
            )
            .padding(28.dp),

        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector =
                    Icons.Default.CalendarMonth,

                contentDescription = null,

                tint = Primary,

                modifier =
                    Modifier.size(34.dp)
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text =
                    "No events for this day",

                color = TextPrimary,

                fontSize = 15.sp,

                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "Your schedule is clear",

                color = TextSecondary,

                fontSize = 12.sp
            )
        }
    }
}

// ============================================================================
// EMPTY UPCOMING
// ============================================================================

@Composable
private fun EmptyUpcoming() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(18.dp)
            )
            .background(Surface)
            .padding(24.dp),

        contentAlignment =
            Alignment.Center
    ) {

        Text(
            text =
                "No upcoming events",

            color = TextSecondary,

            fontSize = 13.sp
        )
    }
}

// ============================================================================
// DECORATIVE WAVE
// ============================================================================

@Composable
private fun DecorativeWave() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {

        repeat(3) { index ->

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(
                        Alignment.Center
                    )
                    .padding(
                        horizontal =
                            (index * 16).dp
                    )
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,

                                Primary.copy(
                                    alpha =
                                        0.18f -
                                                index * 0.04f
                                ),

                                Color(0xFFFF4FD8)
                                    .copy(
                                        alpha = 0.12f
                                    ),

                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

// ============================================================================
// COLOR HELPER
// ============================================================================

private fun parseColor(
    value: String
): Color? {

    return runCatching {

        Color(
            android.graphics.Color.parseColor(
                value
            )
        )

    }.getOrNull()
}