package com.example.loadsheddingapp.ui.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loadsheddingapp.ui.components.ErrorMessage
import com.example.loadsheddingapp.ui.components.LoadingIndicator
import com.example.loadsheddingapp.ui.components.ScheduleCard
import com.example.loadsheddingapp.ui.components.getStageColor
import com.example.loadsheddingapp.ui.viewmodel.ScheduleViewModel

// ScheduleDetailScreen displaying daily and weekly load-shedding schedules for a selected suburb.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailScreen(
    suburbId: String,
    scheduleViewModel: ScheduleViewModel,
    onNavigateBack: () -> Unit
) {
    val scheduleUiState by scheduleViewModel.scheduleUiState.collectAsState()

    LaunchedEffect(suburbId) {
        scheduleViewModel.loadScheduleForSuburb(suburbId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(scheduleUiState.suburb?.name ?: "Schedule Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { scheduleViewModel.loadScheduleForSuburb(suburbId) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Schedule")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Suburb Summary Header Card
            val suburb = scheduleUiState.suburb
            if (suburb != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = suburb.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${suburb.municipality}, ${suburb.province}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(getStageColor(suburb.currentStage), shape = CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (suburb.currentStage == 0) "No Shedding" else "Stage ${suburb.currentStage}",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Offline Cache Notice
            if (scheduleUiState.isCachedData) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Text(
                        text = "Viewing cached schedule stored in local Room Database.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Error Display
            if (scheduleUiState.errorMessage != null && !scheduleUiState.isCachedData) {
                ErrorMessage(
                    message = scheduleUiState.errorMessage!!,
                    onRetry = { scheduleViewModel.loadScheduleForSuburb(suburbId) }
                )
            }

            // Loading Indicator
            if (scheduleUiState.isLoading) {
                LoadingIndicator(message = "Loading schedule...")
            } else {
                Text(
                    text = "Upcoming Load-Shedding Slots",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (scheduleUiState.schedules.isEmpty()) {
                    Text(
                        text = "No upcoming load-shedding slots scheduled.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(scheduleUiState.schedules) { schedule ->
                            ScheduleCard(schedule = schedule)
                        }
                    }
                }
            }
        }
    }
}
