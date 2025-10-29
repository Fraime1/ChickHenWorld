package com.chikegam.henwoldir.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chikegam.henwoldir.data.model.*
import com.chikegam.henwoldir.ui.theme.*
import com.chikegam.henwoldir.ui.viewmodel.BreedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    viewModel: BreedViewModel,
    onNavigateToBreed: (String) -> Unit
) {
    val breeds by viewModel.breeds.collectAsState()
    var sortOption by remember { mutableStateOf(SortOption.ALPHABETICAL) }
    var showSortMenu by remember { mutableStateOf(false) }

    val sortedBreeds = remember(breeds, sortOption) {
        when (sortOption) {
            SortOption.ALPHABETICAL -> breeds.sortedBy { it.name }
            SortOption.BY_COUNTRY -> breeds.sortedBy { it.origin }
            SortOption.BY_TYPE -> breeds.sortedBy { it.type.name }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "My Collection",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${breeds.count { it.isUnlocked }} / ${breeds.size} breeds",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }

                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Sort",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = {
                                    sortOption = option
                                    showSortMenu = false
                                },
                                leadingIcon = {
                                    if (sortOption == option) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Grid of Breeds
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sortedBreeds) { breed ->
                BreedCollectionCard(
                    breed = breed,
                    onClick = {
                        if (breed.isUnlocked) {
                            onNavigateToBreed(breed.id)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BreedCollectionCard(breed: Breed, onClick: () -> Unit) {
    val breedTypeColor = when (breed.type) {
        com.chikegam.henwoldir.data.model.BreedType.EGG_LAYING -> EggLayerColor
        com.chikegam.henwoldir.data.model.BreedType.MEAT -> MeatColor
        com.chikegam.henwoldir.data.model.BreedType.DUAL_PURPOSE -> DualPurposeColor
        com.chikegam.henwoldir.data.model.BreedType.ORNAMENTAL -> OrnamentalColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable(enabled = breed.isUnlocked, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (breed.isUnlocked) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (breed.isUnlocked) {
                        Brush.verticalGradient(
                            colors = listOf(
                                breedTypeColor.copy(alpha = 0.1f),
                                breedTypeColor.copy(alpha = 0.05f)
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .alpha(if (breed.isUnlocked) 1f else 0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Enhanced Image/Icon with background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (breed.isUnlocked) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (breed.isUnlocked) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🐔",
                                style = MaterialTheme.typography.displayMedium,
                                fontSize = MaterialTheme.typography.displayMedium.fontSize * 1.8f
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = breedTypeColor.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = breed.type.emoji,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "???",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Enhanced Name
                Text(
                    text = if (breed.isUnlocked) breed.name else "???",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Enhanced Type Badge
                if (breed.isUnlocked) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = breedTypeColor.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = breed.type.emoji,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = breed.type.displayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = breedTypeColor,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Locked",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Enhanced Unlocked Badge
            if (breed.isUnlocked) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(28.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        )
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Unlocked",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

enum class SortOption(val displayName: String) {
    ALPHABETICAL("Alphabetical"),
    BY_COUNTRY("By Country"),
    BY_TYPE("By Type")
}

