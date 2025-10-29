package com.chikegam.henwoldir.data.model

data class Breed(
    val id: String,
    val name: String,
    val scientificName: String,
    val origin: String,
    val region: Region,
    val type: BreedType,
    val size: BreedSize,
    val imageUrl: String,
    val description: String,
    val appearance: String,
    val behavior: String,
    val productivity: Productivity,
    val interestingFacts: List<String>,
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null
)

data class Productivity(
    val eggsPerYear: Int,
    val eggColor: String,
    val weight: String,
    val meatQuality: String
)

enum class BreedType(val displayName: String, val emoji: String) {
    EGG_LAYING("Egg Layer", "🥚"),
    MEAT("Meat", "🍗"),
    DUAL_PURPOSE("Dual Purpose", "🐥"),
    ORNAMENTAL("Ornamental", "✨")
}

enum class BreedSize(val displayName: String) {
    BANTAM("Bantam"),
    STANDARD("Standard"),
    LARGE("Large")
}

enum class Region(val displayName: String) {
    ASIA("Asia"),
    EUROPE("Europe"),
    NORTH_AMERICA("North America"),
    SOUTH_AMERICA("South America"),
    AFRICA("Africa"),
    OCEANIA("Oceania")
}

