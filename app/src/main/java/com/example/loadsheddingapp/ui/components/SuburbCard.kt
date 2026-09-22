package com.example.loadsheddingapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loadsheddingapp.domain.model.Suburb
import com.example.loadsheddingapp.ui.theme.Stage0Color
import com.example.loadsheddingapp.ui.theme.Stage1Color
import com.example.loadsheddingapp.ui.theme.Stage2Color
import com.example.loadsheddingapp.ui.theme.Stage3Color
import com.example.loadsheddingapp.ui.theme.Stage4Color
import com.example.loadsheddingapp.ui.theme.Stage5Color
import com.example.loadsheddingapp.ui.theme.Stage6PlusColor

// Helper function returning Stage color indicator
fun getStageColor(stage: Int): Color {
    return when (stage) {
        0 -> Stage0Color
        1 -> Stage1Color
        2 -> Stage2Color
        3 -> Stage3Color
        4 -> Stage4Color
        5 -> Stage5Color
        else -> Stage6PlusColor
    }
}

// Reusable card component displaying suburb information and load-shedding stage status.
@Composable
fun SuburbCard(
    suburb: Suburb,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onToggleFavorite: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = suburb.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${suburb.municipality}, ${suburb.province}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                if (!suburb.customLabel.isNull_or_blank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Label: ${suburb.customLabel}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // Stage Badge
            Box(
                modifier = Modifier
                    .background(
                        color = getStageColor(suburb.currentStage),
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (suburb.currentStage == 0) "No Shedding" else "Stage ${suburb.currentStage}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            if (onToggleFavorite != null) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (suburb.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite Icon",
                        tint = if (suburb.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Icon",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

// Extension to check isNullOrBlank cleanly
private fun String?.isNull_or_blank(): Boolean = this == null || this.trim().isEmpty()
