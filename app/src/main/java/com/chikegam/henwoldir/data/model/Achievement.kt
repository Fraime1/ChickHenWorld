package com.chikegam.henwoldir.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null,
    val requirement: AchievementRequirement
)

sealed class AchievementRequirement {
    data class BreedCount(val count: Int) : AchievementRequirement()
    data class RegionComplete(val region: Region) : AchievementRequirement()
    data class TypeComplete(val type: BreedType) : AchievementRequirement()
    object FirstBreed : AchievementRequirement()
}

