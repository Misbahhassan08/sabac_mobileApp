package com.example.carapp.screens

data class CarBrand(
    val name: String,
    val models: List<String>
)



val carBrands = listOf(
    CarBrand(
        name = "Toyota",
        models = listOf(
            "Corolla", "Camry", "Yaris", "Land Cruiser", "Hilux",
            "Prius", "Avalon", "RAV4", "Supra"
        )
    ),
    CarBrand(
        name = "Honda",
        models = listOf(
            "Civic", "Accord", "CR-V", "City", "Fit",
            "HR-V", "Odyssey", "Pilot", "Ridgeline"
        )
    ),
    CarBrand(
        name = "Nissan",
        models = listOf(
            "Altima", "Sentra", "Maxima", "Pathfinder", "Juke",
            "Murano", "Rogue", "Titan", "Versa"
        )
    ),
    CarBrand(
        name = "Ford",
        models = listOf(
            "Mustang", "F-150", "Explorer", "Escape", "Edge",
            "Fusion", "Bronco", "Expedition", "Ranger"
        )
    ),
    CarBrand(
        name = "Mercedes",
        models = listOf(
            "C-Class", "E-Class", "GLA", "S-Class", "GLE",
            "GLC", "A-Class", "G-Class", "AMG GT"
        )
    ),
    CarBrand(
        name = "Hyundai",
        models = listOf(
            "Elantra", "Tucson", "Santa Fe", "Sonata", "Palisade",
            "Kona", "Venue", "Ioniq", "Accent"
        )
    )
)


val carNamesMap = mapOf(
    "Honda" to listOf( "Civic", "Accord", "CR-V", "City", "Fit",
        "HR-V", "Odyssey", "Pilot", "Ridgeline"),
    "Toyota" to listOf("Corolla", "Camry", "Yaris", "Land Cruiser", "Hilux",
        "Prius", "Avalon", "RAV4", "Supra"),
    "Hyundai" to listOf("Elantra", "Tucson", "Santa Fe", "Sonata", "Palisade",
        "Kona", "Venue", "Ioniq", "Accent")
)