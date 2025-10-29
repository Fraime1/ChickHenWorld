package com.chikegam.henwoldir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chikegam.henwoldir.data.model.*
import com.chikegam.henwoldir.data.repository.BreedRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BreedViewModel : ViewModel() {
    private val repository = BreedRepository()

    val breeds: StateFlow<List<Breed>> = repository.breeds
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val unlockedBreeds: StateFlow<List<Breed>> = repository.unlockedBreeds
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val lockedBreeds: StateFlow<List<Breed>> = repository.lockedBreeds
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _dailyBreed = MutableStateFlow<Breed?>(null)
    val dailyBreed: StateFlow<Breed?> = _dailyBreed.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedRegion = MutableStateFlow<Region?>(null)
    val selectedRegion: StateFlow<Region?> = _selectedRegion.asStateFlow()

    private val _selectedType = MutableStateFlow<BreedType?>(null)
    val selectedType: StateFlow<BreedType?> = _selectedType.asStateFlow()

    private val _selectedSize = MutableStateFlow<BreedSize?>(null)
    val selectedSize: StateFlow<BreedSize?> = _selectedSize.asStateFlow()

    val filteredBreeds: StateFlow<List<Breed>> = combine(
        breeds,
        _searchQuery,
        _selectedRegion,
        _selectedType,
        _selectedSize
    ) { breeds, query, region, type, size ->
        repository.searchBreeds(query, region, type, size)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadDailyBreed()
    }

    private fun loadDailyBreed() {
        viewModelScope.launch {
            _dailyBreed.value = repository.getDailyBreed()
        }
    }

    fun unlockBreed(breedId: String) {
        viewModelScope.launch {
            repository.unlockBreed(breedId)
            loadDailyBreed() // Refresh daily breed after unlocking
        }
    }

    fun getBreedById(id: String): Breed? {
        return repository.getBreedById(id)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectRegion(region: Region?) {
        _selectedRegion.value = region
    }

    fun selectType(type: BreedType?) {
        _selectedType.value = type
    }

    fun selectSize(size: BreedSize?) {
        _selectedSize.value = size
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedRegion.value = null
        _selectedType.value = null
        _selectedSize.value = null
    }

    fun getCollectionProgress(): Pair<Int, Int> {
        val unlocked = unlockedBreeds.value.size
        val total = breeds.value.size
        return Pair(unlocked, total)
    }
}

