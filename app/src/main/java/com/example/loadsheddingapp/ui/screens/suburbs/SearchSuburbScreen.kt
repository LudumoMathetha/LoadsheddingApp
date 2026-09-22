package com.example.loadsheddingapp.ui.screens.suburbs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loadsheddingapp.ui.components.ErrorMessage
import com.example.loadsheddingapp.ui.components.LoadingIndicator
import com.example.loadsheddingapp.ui.components.SuburbCard
import com.example.loadsheddingapp.ui.viewmodel.SuburbViewModel

// SearchSuburbScreen allowing users to query REST API suburbs and add them to Room storage.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSuburbScreen(
    suburbViewModel: SuburbViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSchedule: (String) -> Unit
) {
    val searchUiState by suburbViewModel.searchUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Suburbs", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Search Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchUiState.searchQuery,
                    onValueChange = { suburbViewModel.onSearchQueryChanged(it) },
                    label = { Text("Search Suburb or Municipality") },
                    placeholder = { Text("e.g. Sandton, Randburg, Sea Point") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    trailingIcon = {
                        if (searchUiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { suburbViewModel.onSearchQueryChanged("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { suburbViewModel.searchSuburbs() }),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { suburbViewModel.searchSuburbs() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Trigger Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notification Banner when Suburb is added or exists
            if (searchUiState.successNotification != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = searchUiState.successNotification!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { suburbViewModel.clearNotification() }) {
                            Icon(Icons.Default.Clear, contentDescription = "Dismiss")
                        }
                    }
                }
            }

            // Error Display
            if (searchUiState.errorMessage != null) {
                ErrorMessage(
                    message = searchUiState.errorMessage!!,
                    onRetry = { suburbViewModel.searchSuburbs() }
                )
            }

            // Loading Indicator
            if (searchUiState.isLoading) {
                LoadingIndicator(message = "Searching suburbs...")
            } else {
                // Results List
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(searchUiState.searchResults) { suburb ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    SuburbCard(
                                        suburb = suburb,
                                        onClick = { onNavigateToSchedule(suburb.suburbId) }
                                    )
                                }

                                Button(
                                    onClick = { suburbViewModel.saveSuburb(suburb) },
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Add", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
