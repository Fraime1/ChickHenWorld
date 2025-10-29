package com.chikegam.henwoldir.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chikegam.henwoldir.data.model.*
import com.chikegam.henwoldir.ui.theme.*
import com.chikegam.henwoldir.ui.viewmodel.BreedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: BreedViewModel,
    onNavigateToBreed: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filteredBreeds by viewModel.filteredBreeds.collectAsState()
    val selectedRegion by viewModel.selectedRegion.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val selectedSize by viewModel.selectedSize.collectAsState()

    var showFilterDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with Search Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Search Breeds",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search breeds...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors =  TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor =  androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedIndicatorColor =  androidx.compose.ui.graphics.Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = MaterialTheme.colorScheme.surface,
//                        focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
//                        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
//                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Filter Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Active Filters
                    val activeFiltersCount = listOfNotNull(selectedRegion, selectedType, selectedSize).size
                    Text(
                        text = if (activeFiltersCount > 0) "$activeFiltersCount filter(s) active" else "No filters",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )

                    Row {
                        if (activeFiltersCount > 0) {
                            TextButton(
                                onClick = { viewModel.clearFilters() },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text("Clear")
                            }
                        }

                        Button(
                            onClick = { showFilterDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filters",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Filters")
                        }
                    }
                }
            }
        }

        // Results
        if (filteredBreeds.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(
                        text = "🔍",
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = MaterialTheme.typography.displayLarge.fontSize * 2
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No breeds found",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Try adjusting your search or filters",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "${filteredBreeds.size} breed(s) found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(filteredBreeds) { breed ->
                    SearchResultCard(
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

    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            selectedRegion = selectedRegion,
            selectedType = selectedType,
            selectedSize = selectedSize,
            onRegionSelected = { viewModel.selectRegion(it) },
            onTypeSelected = { viewModel.selectType(it) },
            onSizeSelected = { viewModel.selectSize(it) },
            onDismiss = { showFilterDialog = false }
        )
    }
}

@Composable
fun SearchResultCard(breed: Breed, onClick: () -> Unit) {
    val breedTypeColor = when (breed.type) {
        com.chikegam.henwoldir.data.model.BreedType.EGG_LAYING -> EggLayerColor
        com.chikegam.henwoldir.data.model.BreedType.MEAT -> MeatColor
        com.chikegam.henwoldir.data.model.BreedType.DUAL_PURPOSE -> DualPurposeColor
        com.chikegam.henwoldir.data.model.BreedType.ORNAMENTAL -> OrnamentalColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = breed.isUnlocked, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (breed.isUnlocked) 6.dp else 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (breed.isUnlocked) {
                        Brush.verticalGradient(
                            colors = listOf(
                                breedTypeColor.copy(alpha = 0.05f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Enhanced Image with type color
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (breed.isUnlocked) {
                                Brush.radialGradient(
                                    colors = listOf(
                                        breedTypeColor.copy(alpha = 0.2f),
                                        breedTypeColor.copy(alpha = 0.1f)
                                    )
                                )
                            } else {
                                Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    )
                                )
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
                                style = MaterialTheme.typography.headlineMedium,
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize * 1.2f
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = breed.type.emoji,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "???",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                // Enhanced Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (breed.isUnlocked) breed.name else "???",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (breed.isUnlocked) {
                        // Origin with icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = breed.origin,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Enhanced badges
                        Column (verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = breedTypeColor.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = breed.type.emoji,
                                        style = MaterialTheme.typography.labelSmall
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

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = breed.size.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Enhanced chevron
                if (breed.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "View details",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    selectedRegion: Region?,
    selectedType: BreedType?,
    selectedSize: BreedSize?,
    onRegionSelected: (Region?) -> Unit,
    onTypeSelected: (BreedType?) -> Unit,
    onSizeSelected: (BreedSize?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter Breeds") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Region Filter
                FilterSection(
                    title = "Region",
                    items = Region.values().toList(),
                    selectedItem = selectedRegion,
                    onItemSelected = onRegionSelected,
                    itemLabel = { it?.displayName ?: "All" }
                )

                Divider()

                // Type Filter
                FilterSection(
                    title = "Type",
                    items = BreedType.values().toList(),
                    selectedItem = selectedType,
                    onItemSelected = onTypeSelected,
                    itemLabel = { it?.displayName ?: "All" }
                )

                Divider()

                // Size Filter
                FilterSection(
                    title = "Size",
                    items = BreedSize.values().toList(),
                    selectedItem = selectedSize,
                    onItemSelected = onSizeSelected,
                    itemLabel = { it?.displayName ?: "All" }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}

@Composable
fun <T> FilterSection(
    title: String,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T?) -> Unit,
    itemLabel: (T?) -> String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedItem == null,
                onClick = { onItemSelected(null) },
                label = { Text("All", style = MaterialTheme.typography.labelSmall) }
            )

            items.take(3).forEach { item ->
                FilterChip(
                    selected = selectedItem == item,
                    onClick = { onItemSelected(if (selectedItem == item) null else item) },
                    label = { Text(itemLabel(item), style = MaterialTheme.typography.labelSmall) }
                )
            }
        }

        if (items.size > 3) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.drop(3).forEach { item ->
                    FilterChip(
                        selected = selectedItem == item,
                        onClick = { onItemSelected(if (selectedItem == item) null else item) },
                        label = { Text(itemLabel(item), style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }
        }
    }
}

