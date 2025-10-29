package com.chikegam.henwoldir.data.repository

import com.chikegam.henwoldir.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class AchievementRepository(private val breedRepository: BreedRepository) {
    private val _achievements = MutableStateFlow<List<Achievement>>(getAllAchievements())
    val achievements: Flow<List<Achievement>> = _achievements

    init {
        // Update achievements based on breed collection
        combine(
            _achievements,
            breedRepository.breeds
        ) { achievements, breeds ->
            updateAchievements(achievements, breeds)
        }
    }

    private fun updateAchievements(achievements: List<Achievement>, breeds: List<Breed>): List<Achievement> {
        val unlockedCount = breeds.count { it.isUnlocked }
        
        return achievements.map { achievement ->
            val shouldUnlock = when (achievement.requirement) {
                is AchievementRequirement.FirstBreed -> unlockedCount >= 1
                is AchievementRequirement.BreedCount -> unlockedCount >= achievement.requirement.count
                is AchievementRequirement.RegionComplete -> {
                    val regionBreeds = breeds.filter { it.region == achievement.requirement.region }
                    regionBreeds.isNotEmpty() && regionBreeds.all { it.isUnlocked }
                }
                is AchievementRequirement.TypeComplete -> {
                    val typeBreeds = breeds.filter { it.type == achievement.requirement.type }
                    typeBreeds.isNotEmpty() && typeBreeds.all { it.isUnlocked }
                }
            }
            
            if (shouldUnlock && !achievement.isUnlocked) {
                achievement.copy(isUnlocked = true, unlockedDate = System.currentTimeMillis())
            } else {
                achievement
            }
        }
    }

    private fun getAllAchievements(): List<Achievement> {
        return listOf(
            Achievement(
                id = "first",
                title = "First Discovery",
                description = "Unlock your first breed",
                icon = "🐣",
                requirement = AchievementRequirement.FirstBreed
            ),
            Achievement(
                id = "10breeds",
                title = "Chicken Enthusiast",
                description = "Discover 10 breeds",
                icon = "🐔",
                requirement = AchievementRequirement.BreedCount(10)
            ),
            Achievement(
                id = "25breeds",
                title = "Poultry Expert",
                description = "Discover 25 breeds",
                icon = "🏆",
                requirement = AchievementRequirement.BreedCount(25)
            ),
            Achievement(
                id = "50breeds",
                title = "Master Collector",
                description = "Discover 50 breeds",
                icon = "👑",
                requirement = AchievementRequirement.BreedCount(50)
            ),
            Achievement(
                id = "asia_complete",
                title = "Asian Collection Complete",
                description = "Collect all Asian breeds",
                icon = "🏮",
                requirement = AchievementRequirement.RegionComplete(Region.ASIA)
            ),
            Achievement(
                id = "europe_complete",
                title = "European Collection Complete",
                description = "Collect all European breeds",
                icon = "🏰",
                requirement = AchievementRequirement.RegionComplete(Region.EUROPE)
            ),
            Achievement(
                id = "americas_complete",
                title = "Americas Collection Complete",
                description = "Collect all American breeds",
                icon = "🗽",
                requirement = AchievementRequirement.RegionComplete(Region.NORTH_AMERICA)
            )
        )
    }
}

