package com.chikegam.henwoldir.data.repository

import com.chikegam.henwoldir.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class BreedRepository {
    private val _breeds = MutableStateFlow<List<Breed>>(getAllBreeds())
    val breeds: Flow<List<Breed>> = _breeds

    val unlockedBreeds: Flow<List<Breed>> = _breeds.map { breeds ->
        breeds.filter { it.isUnlocked }
    }

    val lockedBreeds: Flow<List<Breed>> = _breeds.map { breeds ->
        breeds.filter { !it.isUnlocked }
    }

    fun unlockBreed(breedId: String) {
        _breeds.update { breeds ->
            breeds.map { breed ->
                if (breed.id == breedId && !breed.isUnlocked) {
                    breed.copy(isUnlocked = true, unlockedDate = System.currentTimeMillis())
                } else {
                    breed
                }
            }
        }
    }

    fun getBreedById(id: String): Breed? {
        return _breeds.value.find { it.id == id }
    }

    fun getDailyBreed(): Breed {
        // Get breed of the day based on current date
        val dayOfYear = (System.currentTimeMillis() / (1000 * 60 * 60 * 24)).toInt()
        val unlockedBreeds = _breeds.value.filter { it.isUnlocked }
        val availableBreeds = if (unlockedBreeds.size < _breeds.value.size) {
            _breeds.value.filter { !it.isUnlocked }
        } else {
            _breeds.value
        }
        val index = dayOfYear % availableBreeds.size
        return availableBreeds[index]
    }

    fun searchBreeds(query: String, region: Region? = null, type: BreedType? = null, size: BreedSize? = null): List<Breed> {
        return _breeds.value.filter { breed ->
            val matchesQuery = query.isEmpty() || 
                breed.name.contains(query, ignoreCase = true) ||
                breed.origin.contains(query, ignoreCase = true)
            val matchesRegion = region == null || breed.region == region
            val matchesType = type == null || breed.type == type
            val matchesSize = size == null || breed.size == size
            
            matchesQuery && matchesRegion && matchesType && matchesSize
        }
    }

    private fun getAllBreeds(): List<Breed> {
        return listOf(
            Breed(
                id = "1",
                name = "Rhode Island Red",
                scientificName = "Gallus gallus domesticus",
                origin = "Rhode Island, USA",
                region = Region.NORTH_AMERICA,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.STANDARD,
                imageUrl = "rhode_island_red",
                description = "One of the most famous American breeds, known for their hardiness and excellent egg production.",
                appearance = "Deep mahogany red plumage, with black tail feathers and red comb. Strong, rectangular body shape.",
                behavior = "Hardy, adaptable, and friendly. Great foragers with a calm temperament.",
                productivity = Productivity(
                    eggsPerYear = 250,
                    eggColor = "Brown",
                    weight = "3.0-3.9 kg",
                    meatQuality = "Good"
                ),
                interestingFacts = listOf(
                    "Named after Rhode Island state",
                    "Can lay eggs even in winter",
                    "State bird of Rhode Island since 1954"
                ),
                isUnlocked = true
            ),
            Breed(
                id = "2",
                name = "Leghorn",
                scientificName = "Gallus gallus domesticus",
                origin = "Tuscany, Italy",
                region = Region.EUROPE,
                type = BreedType.EGG_LAYING,
                size = BreedSize.STANDARD,
                imageUrl = "leghorn",
                description = "The most efficient egg-laying breed, originating from Italy but perfected in America.",
                appearance = "Usually white plumage, large single red comb, yellow legs. Sleek and elegant body.",
                behavior = "Active, flighty, and independent. Not very friendly but excellent foragers.",
                productivity = Productivity(
                    eggsPerYear = 320,
                    eggColor = "White",
                    weight = "2.0-2.7 kg",
                    meatQuality = "Fair"
                ),
                interestingFacts = listOf(
                    "Can lay up to 320 eggs per year",
                    "Named after the Italian city of Livorno (Leghorn in English)",
                    "Most commercial white eggs come from Leghorns"
                ),
                isUnlocked = true
            ),
            Breed(
                id = "3",
                name = "Silkie",
                scientificName = "Gallus gallus domesticus",
                origin = "China",
                region = Region.ASIA,
                type = BreedType.ORNAMENTAL,
                size = BreedSize.BANTAM,
                imageUrl = "silkie",
                description = "An ancient breed with unique fluffy plumage that feels like silk or satin.",
                appearance = "Fluffy, hair-like plumage in white, black, blue, buff, or partridge. Black skin and bones, blue earlobes, five toes.",
                behavior = "Calm, friendly, and broody. Excellent mothers and great pets for children.",
                productivity = Productivity(
                    eggsPerYear = 100,
                    eggColor = "Cream/Tinted",
                    weight = "0.9-1.4 kg",
                    meatQuality = "Delicacy in Asia"
                ),
                interestingFacts = listOf(
                    "Has black skin, bones, and meat",
                    "Five toes instead of the usual four",
                    "Believed to have medicinal properties in traditional Chinese medicine"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "4",
                name = "Plymouth Rock",
                scientificName = "Gallus gallus domesticus",
                origin = "Massachusetts, USA",
                region = Region.NORTH_AMERICA,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.LARGE,
                imageUrl = "plymouth_rock",
                description = "An iconic American breed with distinctive barred plumage pattern.",
                appearance = "Black and white barred pattern (most common), also comes in other colors. Large, rectangular body.",
                behavior = "Friendly, docile, and calm. Great for families and beginners.",
                productivity = Productivity(
                    eggsPerYear = 200,
                    eggColor = "Brown",
                    weight = "3.4-4.3 kg",
                    meatQuality = "Excellent"
                ),
                interestingFacts = listOf(
                    "One of the oldest American breeds",
                    "Was the most popular farm chicken in the USA for decades",
                    "Named after Plymouth Rock where the Pilgrims landed"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "5",
                name = "Orpington",
                scientificName = "Gallus gallus domesticus",
                origin = "England",
                region = Region.EUROPE,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.LARGE,
                imageUrl = "orpington",
                description = "A large, gentle English breed known for their fluffy appearance and friendly nature.",
                appearance = "Fluffy, abundant plumage in buff, black, blue, or white. Large, broad body with small head.",
                behavior = "Very friendly, docile, and gentle. Perfect for beginners and families. Can become broody.",
                productivity = Productivity(
                    eggsPerYear = 180,
                    eggColor = "Light Brown",
                    weight = "3.6-4.5 kg",
                    meatQuality = "Excellent"
                ),
                interestingFacts = listOf(
                    "Created by William Cook in Orpington, Kent in 1886",
                    "Originally bred to be the perfect dual-purpose bird",
                    "Their fluffy feathers make them look bigger than they are"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "6",
                name = "Brahma",
                scientificName = "Gallus gallus domesticus",
                origin = "United States (from Asian stock)",
                region = Region.NORTH_AMERICA,
                type = BreedType.MEAT,
                size = BreedSize.LARGE,
                imageUrl = "brahma",
                description = "One of the largest chicken breeds, known as the 'King of Chickens'.",
                appearance = "Massive size with feathered legs and feet. Colors include light, dark, and buff. Pea comb.",
                behavior = "Gentle giants, calm and docile despite their size. Good with children.",
                productivity = Productivity(
                    eggsPerYear = 150,
                    eggColor = "Brown",
                    weight = "4.5-5.5 kg",
                    meatQuality = "Excellent"
                ),
                interestingFacts = listOf(
                    "Can weigh up to 6 kg or more",
                    "Feathered feet and legs protect them in cold weather",
                    "Named after the Brahmaputra River in India"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "7",
                name = "Sussex",
                scientificName = "Gallus gallus domesticus",
                origin = "Sussex, England",
                region = Region.EUROPE,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.STANDARD,
                imageUrl = "sussex",
                description = "An old English breed prized for both meat and egg production.",
                appearance = "Speckled, light, or red colored. Rectangular body with a small head and single comb.",
                behavior = "Friendly, curious, and alert. Good foragers and easy to handle.",
                productivity = Productivity(
                    eggsPerYear = 250,
                    eggColor = "Light Brown/Cream",
                    weight = "3.2-4.1 kg",
                    meatQuality = "Excellent"
                ),
                interestingFacts = listOf(
                    "One of the oldest breeds in England",
                    "Mentioned in Roman writings about Britain",
                    "Can adapt to both free-range and confined conditions"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "8",
                name = "Wyandotte",
                scientificName = "Gallus gallus domesticus",
                origin = "New York, USA",
                region = Region.NORTH_AMERICA,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.STANDARD,
                imageUrl = "wyandotte",
                description = "An American breed with beautiful laced feather patterns and rose comb.",
                appearance = "Various colors with laced patterns. Rose comb, yellow legs, and rounded body shape.",
                behavior = "Hardy, docile, and friendly. Good layers even in winter.",
                productivity = Productivity(
                    eggsPerYear = 200,
                    eggColor = "Brown",
                    weight = "3.0-3.9 kg",
                    meatQuality = "Good"
                ),
                interestingFacts = listOf(
                    "Named after the Wyandot Native American tribe",
                    "Rose comb is resistant to frostbite",
                    "Silver Laced variety is the most popular"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "9",
                name = "Cochin",
                scientificName = "Gallus gallus domesticus",
                origin = "China",
                region = Region.ASIA,
                type = BreedType.ORNAMENTAL,
                size = BreedSize.LARGE,
                imageUrl = "cochin",
                description = "A large, fluffy breed with heavily feathered legs, originally from China.",
                appearance = "Extremely fluffy with abundant soft feathers. Fully feathered legs and feet. Various colors.",
                behavior = "Very calm, gentle, and friendly. Goes broody easily. Perfect for beginners.",
                productivity = Productivity(
                    eggsPerYear = 150,
                    eggColor = "Brown",
                    weight = "3.9-5.0 kg",
                    meatQuality = "Good"
                ),
                interestingFacts = listOf(
                    "Caused 'Hen Fever' craze in England and America in 1850s",
                    "Given as gifts to Queen Victoria",
                    "Can handle cold weather extremely well"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "10",
                name = "Marans",
                scientificName = "Gallus gallus domesticus",
                origin = "Marans, France",
                region = Region.EUROPE,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.STANDARD,
                imageUrl = "marans",
                description = "Famous French breed known for laying the darkest brown eggs.",
                appearance = "Various colors including Black Copper, with feathered or clean legs depending on variety.",
                behavior = "Active, hardy, and good foragers. Can be flighty but generally calm.",
                productivity = Productivity(
                    eggsPerYear = 180,
                    eggColor = "Dark Chocolate Brown",
                    weight = "3.2-4.0 kg",
                    meatQuality = "Excellent"
                ),
                interestingFacts = listOf(
                    "Lays the darkest brown eggs of any chicken breed",
                    "Eggs are graded on a color scale from 1-9",
                    "Prized by chefs for egg flavor and color"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "11",
                name = "Australorp",
                scientificName = "Gallus gallus domesticus",
                origin = "Australia",
                region = Region.OCEANIA,
                type = BreedType.EGG_LAYING,
                size = BreedSize.STANDARD,
                imageUrl = "australorp",
                description = "Australian breed developed from Black Orpingtons, holds egg-laying records.",
                appearance = "Black plumage with green sheen, large body, red comb and wattles.",
                behavior = "Calm, docile, and friendly. Great for beginners and backyard flocks.",
                productivity = Productivity(
                    eggsPerYear = 300,
                    eggColor = "Light Brown",
                    weight = "2.7-3.9 kg",
                    meatQuality = "Good"
                ),
                interestingFacts = listOf(
                    "World record: 364 eggs in 365 days",
                    "Name is a contraction of 'Australian Orpington'",
                    "National bird of Australia's poultry industry"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "12",
                name = "Ameraucana",
                scientificName = "Gallus gallus domesticus",
                origin = "United States",
                region = Region.NORTH_AMERICA,
                type = BreedType.EGG_LAYING,
                size = BreedSize.STANDARD,
                imageUrl = "ameraucana",
                description = "American breed known for laying blue eggs and having muffs and beards.",
                appearance = "Various colors, distinctive muffs and beard, pea comb. Upright tail.",
                behavior = "Alert, friendly, and hardy. Good foragers with calm temperament.",
                productivity = Productivity(
                    eggsPerYear = 200,
                    eggColor = "Blue",
                    weight = "2.5-3.0 kg",
                    meatQuality = "Fair"
                ),
                interestingFacts = listOf(
                    "Developed from Araucana breed from Chile",
                    "Blue egg color comes from bile pigment oocyanin",
                    "Eight recognized color varieties"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "13",
                name = "Polish",
                scientificName = "Gallus gallus domesticus",
                origin = "Netherlands (not Poland)",
                region = Region.EUROPE,
                type = BreedType.ORNAMENTAL,
                size = BreedSize.STANDARD,
                imageUrl = "polish",
                description = "Striking ornamental breed with a large crest of feathers on their head.",
                appearance = "Large feather crest (like a pompadour), V-shaped comb, various colors including White Crested Black.",
                behavior = "Calm and friendly but can be skittish. Crest can limit vision.",
                productivity = Productivity(
                    eggsPerYear = 120,
                    eggColor = "White",
                    weight = "2.0-2.7 kg",
                    meatQuality = "Fair"
                ),
                interestingFacts = listOf(
                    "Despite the name, likely originated in the Netherlands",
                    "Painted by Dutch artists in the 1600s",
                    "Crest can obstruct vision making them vulnerable to predators"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "14",
                name = "Barnevelder",
                scientificName = "Gallus gallus domesticus",
                origin = "Barneveld, Netherlands",
                region = Region.EUROPE,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.STANDARD,
                imageUrl = "barnevelder",
                description = "Dutch breed known for beautiful dark brown eggs and stunning plumage.",
                appearance = "Double-laced pattern in black and brown (most common), also in other colors. Single comb.",
                behavior = "Docile, friendly, and calm. Good layers even in winter.",
                productivity = Productivity(
                    eggsPerYear = 200,
                    eggColor = "Dark Brown",
                    weight = "2.7-3.5 kg",
                    meatQuality = "Good"
                ),
                interestingFacts = listOf(
                    "Created to lay dark brown eggs for export to England",
                    "Double-laced pattern is genetically complex",
                    "Nearly went extinct after WWII"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "15",
                name = "Faverolle",
                scientificName = "Gallus gallus domesticus",
                origin = "Faverolles, France",
                region = Region.EUROPE,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.STANDARD,
                imageUrl = "faverolle",
                description = "French breed with beard, muffs, and five toes, known for gentle nature.",
                appearance = "Fluffy with beard and muffs, feathered legs, five toes. Salmon or White coloring.",
                behavior = "Extremely docile, gentle, and friendly. Great for children and pets.",
                productivity = Productivity(
                    eggsPerYear = 180,
                    eggColor = "Light Brown/Pinkish",
                    weight = "3.0-4.0 kg",
                    meatQuality = "Excellent"
                ),
                interestingFacts = listOf(
                    "Has five toes instead of four",
                    "Prized table bird in France",
                    "Salmon Faverolles have unique coloring between males and females"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "16",
                name = "Serama",
                scientificName = "Gallus gallus domesticus",
                origin = "Malaysia",
                region = Region.ASIA,
                type = BreedType.ORNAMENTAL,
                size = BreedSize.BANTAM,
                imageUrl = "serama",
                description = "The smallest chicken breed in the world, originating from Malaysia.",
                appearance = "Tiny with upright stance, vertical tail, full breast. Various colors.",
                behavior = "Friendly, confident, and personable. Makes great house pets.",
                productivity = Productivity(
                    eggsPerYear = 180,
                    eggColor = "Cream/Tinted",
                    weight = "0.3-0.6 kg",
                    meatQuality = "Not for meat"
                ),
                interestingFacts = listOf(
                    "Smallest chicken breed - can weigh as little as 250g",
                    "Created by crossing Japanese bantams with Malaysian bantams",
                    "Can be kept as house pets"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "17",
                name = "Hamburg",
                scientificName = "Gallus gallus domesticus",
                origin = "Holland/Germany",
                region = Region.EUROPE,
                type = BreedType.EGG_LAYING,
                size = BreedSize.STANDARD,
                imageUrl = "hamburg",
                description = "Ancient European breed known as 'everlasting layers' with beautiful spangled feathers.",
                appearance = "Rose comb, various colors including Silver Spangled and Golden Spangled. Elegant and active.",
                behavior = "Active, flighty, and independent. Excellent foragers but not very friendly.",
                productivity = Productivity(
                    eggsPerYear = 200,
                    eggColor = "White",
                    weight = "1.8-2.5 kg",
                    meatQuality = "Fair"
                ),
                interestingFacts = listOf(
                    "Called 'everlasting layers' due to consistent laying",
                    "One of the oldest chicken breeds",
                    "Roosters are very beautiful with long tail feathers"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "18",
                name = "Langshan",
                scientificName = "Gallus gallus domesticus",
                origin = "China",
                region = Region.ASIA,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.LARGE,
                imageUrl = "langshan",
                description = "Tall Chinese breed with distinctive U-shaped back and feathered legs.",
                appearance = "Tall with U-shaped back, feathered legs. Black or white plumage with green sheen.",
                behavior = "Calm, docile, and friendly. Good layers and broody.",
                productivity = Productivity(
                    eggsPerYear = 200,
                    eggColor = "Dark Brown/Plum",
                    weight = "3.0-4.3 kg",
                    meatQuality = "Good"
                ),
                interestingFacts = listOf(
                    "One of the tallest chicken breeds",
                    "Name means 'wolf mountain' in Chinese",
                    "Can lay eggs with a plum-colored tint"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "19",
                name = "Welsummer",
                scientificName = "Gallus gallus domesticus",
                origin = "Welsum, Netherlands",
                region = Region.EUROPE,
                type = BreedType.DUAL_PURPOSE,
                size = BreedSize.STANDARD,
                imageUrl = "welsummer",
                description = "Dutch breed famous for large, dark brown speckled eggs.",
                appearance = "Partridge coloring with red-brown and black. Single comb, yellow legs.",
                behavior = "Friendly, intelligent, and active. Good foragers.",
                productivity = Productivity(
                    eggsPerYear = 180,
                    eggColor = "Dark Brown with Speckles",
                    weight = "2.5-3.5 kg",
                    meatQuality = "Good"
                ),
                interestingFacts = listOf(
                    "Eggs often have dark speckles",
                    "The Kellogg's Corn Flakes rooster is a Welsummer",
                    "Developed in the early 1900s"
                ),
                isUnlocked = false
            ),
            Breed(
                id = "20",
                name = "Easter Egger",
                scientificName = "Gallus gallus domesticus",
                origin = "United States (hybrid)",
                region = Region.NORTH_AMERICA,
                type = BreedType.EGG_LAYING,
                size = BreedSize.STANDARD,
                imageUrl = "easter_egger",
                description = "Not a true breed but a variety that lays colorful eggs in blue, green, or pink.",
                appearance = "Highly variable as they're hybrids. Often have muffs, beards, or pea combs.",
                behavior = "Generally friendly, hardy, and adaptable. Great for beginners.",
                productivity = Productivity(
                    eggsPerYear = 250,
                    eggColor = "Blue/Green/Pink/Olive",
                    weight = "2.0-3.0 kg",
                    meatQuality = "Fair"
                ),
                interestingFacts = listOf(
                    "Not a recognized breed, but a type of hybrid",
                    "Can lay eggs in various colors from blue to green to olive",
                    "Very popular in backyard flocks for colorful eggs"
                ),
                isUnlocked = false
            )
        )
    }
}

