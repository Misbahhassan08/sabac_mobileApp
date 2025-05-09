package com.example.carapp.screens.Inspector

import androidx.compose.runtime.remember

object InspectionReportUtils {

    val Body_Parts = listOf(
        "Car Body (Outer)" to listOf(
            "Front Right Fender", "Front Left Fender", "Bonnet", "Front Driver Door",
            "Front Passenger Door", "Rear Right Door", "Rear Right Fender", "Rear Left Fender"
        ),
        "Body Frame" to listOf(
            "Radiator Core Support", "Right Strut Tower Appron", "Left Strut Tower Appron",
            "Right Front Rail", "Left Front Rail", "Cowl Panel Firewall", "Right Pilar Front",
            "Right Pilar Back", "Left Pilar Front", "Left Pilar Back", "Right Front Side Panel",
            "Right Rear Side Panel", "Left Front Side Panel", "Left Rear Side Panel"
        ),
        "Test Drive Remarks" to listOf(
            "Engine Starts Properly", "Engine Health", "Engine Noise", "Gear Shifting",
            "Turning", "Suspention Check", "Exhaust", "Cruise Control", "Stearing Controls",
            "Horn", "Cameras", "Sensors", "Warning Lights"
        ),
        "Doors Check" to listOf(
            "Front Right Door", "Front Left Door", "Rear Right Door", "Rear Left Door"
        ),
        "Interior" to listOf(
            "Stearing Wheel", "Dashboard", "Front Driver Seat", "Front Passenger Seat",
            "Rear Seats", "Roof"
        ),
        "Boot" to listOf("Boot Floor", "Spare Tyre", "Tools")
    )

    val ConditionCatogaries = mapOf(
        // Car Body (Outer)
        "Front Right Fender" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
            "Dents" to listOf("None", "Minor", "Major")
        ),
        "Front Left Fender" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
            "Dents" to listOf("None", "Minor", "Major")
        ),
        "Bonnet" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
        ),
        "Front Driver Door" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
        ),
        "Front Passenger Door" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
        ),
        "Rear Right Door" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
        ),
        "Rear Left Door" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
        ),
        "Rear Left Fender" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
        ),
        "Rear Right Fender" to listOf(
            "Paint" to listOf("Original", "Repainted"),
            "Seals" to listOf("Ok", "Damaged", "Repaired"),
        ),
        // Body Frame
        "Radiator Core Support" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Right Strut Tower Appron" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Left Strut Tower Appron" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Right Front Rail" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Left Front Rail" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Cowl Panel Firewall" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Right Pilar Front" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Right Pilar Back" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Left Pilar Front" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Left Pilar Back" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Right Front Side Panel" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Right Rear Side Panel" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Left Front Side Panel" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        "Left Rear Side Panel" to listOf(
            "Condition" to listOf("Ok", "Damaged", "Repaired")
        ),
        // Test Drive Remarks
        "Engine Starts Properly" to listOf(
            "Status" to listOf("Yes", "No", "With Difficulty")
        ),
        "Engine Health" to listOf(
            "Condition" to listOf("Good", "Average", "Poor")
        ),
        "Gear Shifting" to listOf(
            "Smoothness" to listOf("Smooth", "Rough", "Stuck","Jerk","late")
        ),
        "Turning" to listOf(
            "Condition" to listOf("Normal","Abnormal")
        ),
        "Suspention Check" to listOf(
            "Condition" to listOf("Normal","Abnormal")
        ),
        "Exhaust" to listOf(
            "Condition" to listOf("Normal","Abnormal")
        ),
        "Cruise Control" to listOf(
            "Condition" to listOf("Not Available","Working", "Not Working")
        ),
        "Stearing Controls" to listOf(
            "Condition" to listOf("Working","Not Working", "Not Available")
        ),
        "Horn" to listOf(
            "Condition" to listOf("Working","Not Working", "Not Available")
        ),
        "Cameras" to listOf(
            "Condition" to listOf("Working","Not Working", "Not Available")
        ),
        "Sensors" to listOf(
            "Condition" to listOf("Working","Not Working", "Not Available")
        ),
        "Warning Lights" to listOf(
            "Present" to listOf("Yes", "No")
        ),


        // Boot
        "Boot Floor" to listOf(
            "Condition" to listOf("Clean", "Dirty", "Damaged")
        ),
        "Spare Tyre" to listOf(
            "Status" to listOf("Present", "Missing", "Damaged")
        ),
        "Tools" to listOf(
            "Completeness" to listOf("Complete", "Incomplete", "Missing")
        )
    )

    val Custom_Card_Parts = listOf(
        "Bonnet",
        "Front Driver Door",
        "Front Passenger Door",
        "Rear Right Door",
        "Rear Left Door",
        "Radiator Core Support",
        "Right Strut Tower Appron",
        "Left Strut Tower Appron",
        "Right Front Rail",
        "Left Front Rail",
        "Cowl Panel Firewall",
        "Right Pilar Front",
        "Right Pilar Back",
        "Left Pilar Front",
        "Left Pilar Back",
        "Right Front Side Panel",
        "Left Front Side Panel",
        "Right Back Side Panel",
        "Left Back Side Panel",
        "Warning Lights",
        "Stearing Controls",
        "Dashboard",
        "Front Driver Seat",
        "Front Passenger Seat",
        "Rear Seats",
        "Roof",
        "Boot Floor",
    )

}
