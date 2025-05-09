package com.example.carapp.screens.Inspector

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf

/*val carInspectionData = remember {
       mapOf(
           "Car Detail" to mapOf(
               "Body Parts Inspection" to mapOf(
                   "Car Body (Outer)" to mapOf(
                       "Front Right Fender" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "Dents" to mapOf("None" to 0, "Minor" to 1, "Major" to 0)
                       ),
                       "Front Left Fender" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "Dents" to mapOf("None" to 0, "Minor" to 1, "Major" to 0)
                       ),
                       "Bonnet" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Front Driver Door" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Front Passenger Door" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Rear Right Door" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Rear Right Fender" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Rear Left Fender" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                   ),
                   "Body Frame" to mapOf(
                       "Radiator Core Support" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Right Strut Tower Appron" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Left Strut Tower Appron" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Right Front Rail" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Left Front Rail" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Cowl Panel Firewall" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Right Pilar Front" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Right Pilar Back" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Left Pilar Front" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Left Pilar Back" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Right Front Side Panel" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Right Rear Side Panel" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Left Front Side Panel" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Left Rear Side Panel" to mapOf(
                           "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                           "img_urls" to listOf("")
                       ),
                   ),
                   "Test Drive Remarks" to mapOf(
                       "Engine Starts Properly" to mapOf(
                           "Status" to mapOf("Yes" to 0, "No" to 1, "With Difficulty" to 0)
                       ),
                       "Engine Health" to mapOf("Condition" to mapOf("Good" to 0, "Average" to 1, "Poor" to 0)),
                       "Gear Shifting" to mapOf(
                           "Smoothness" to mapOf(
                               "Smooth" to 0,
                               "Rough" to 0,
                               "Stuck" to 1,
                               "Jerk" to 0,
                               "late" to 0,
                           )
                       ),
                       "Turning" to mapOf("Condition" to mapOf("Normal" to 1, "Abnormal" to 0)),
                       "Suspention Check" to mapOf("Condition" to mapOf("Normal" to 1, "Abnormal" to 0)),
                       "Exhaust" to mapOf("Condition" to mapOf("Normal" to 1, "Abnormal" to 0)),
                       "Cruise Control" to mapOf(
                           "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                       ),
                       "Stearing Controls" to mapOf(
                           "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                       ),
                       "Horn" to mapOf(
                           "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                       ),
                       "Cameras" to mapOf(
                           "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                       ),
                       "Sensors" to mapOf(
                           "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                       ),
                       "Warning Lights" to mapOf("Present" to mapOf("Yes" to 0, "No" to 1)),
                   ),
                   "Doors Check" to mapOf(
                       "Front Right Door" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                       ),
                       "Front Left Door" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                       ),
                       "Rear Right Door" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                       ),
                       "Rear Left Door" to mapOf(
                           "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                           "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                       ),
                   ),
                   "Interior" to mapOf(
                       "Stearing Wheel" to mapOf(
                           "Condition" to mapOf("Damage" to 1, "Covered" to 0, "Normal" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Dashboard" to mapOf(
                           "Condition" to mapOf("Damage" to 0, "Normal" to 1, "Faded" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Front Driver Seat" to mapOf(
                           "Condition" to mapOf("Damage" to 0, "Normal" to 1),
                           "img_urls" to listOf("")
                       ),
                       "Front Passenger Seat" to mapOf(
                           "Condition" to mapOf("Damage" to 0, "Normal" to 1),
                           "img_urls" to listOf("")
                       ),
                       "Rear Seats" to mapOf(
                           "Condition" to mapOf("Damage" to 0, "Normal" to 1),
                           "img_urls" to listOf("")
                       ),
                       "Roof" to mapOf(
                           "Condition" to mapOf("Damage" to 0, "Normal" to 1, "Dirty" to 0),
                           "img_urls" to listOf("")
                       ),
                   ),
                   "Boot" to mapOf(
                       "Boot Floor" to mapOf(
                           "Condition" to mapOf("Clean" to 0, "Dirty" to 1, "Damaged" to 0),
                           "img_urls" to listOf("")
                       ),
                       "Spare Tyre" to mapOf("Status" to mapOf("Present" to 0, "Missing" to 1, "Damaged" to 0)),
                       "Tools" to mapOf(
                           "Completeness" to mapOf("Complete" to 0, "Incomplete" to 0, "Missing" to 1)
                       ),
                   ),
               )
           )
       )
   }*/


object CarInspectionData {
    val carInspectionData =
        mapOf(
            "Car Detail" to mapOf(
                "Body Parts Inspection" to mapOf(
                    "Car Body (Outer)" to mapOf(
                        "Front Right Fender" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "Dents" to mapOf("None" to 0, "Minor" to 1, "Major" to 0)
                        ),
                        "Front Left Fender" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "Dents" to mapOf("None" to 0, "Minor" to 1, "Major" to 0)
                        ),
                        "Bonnet" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Front Driver Door" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Front Passenger Door" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Rear Right Door" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Rear Right Fender" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Rear Left Fender" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                    ),
                    "Body Frame" to mapOf(
                        "Radiator Core Support" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Right Strut Tower Appron" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Left Strut Tower Appron" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Right Front Rail" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Left Front Rail" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Cowl Panel Firewall" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Right Pilar Front" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Right Pilar Back" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Left Pilar Front" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Left Pilar Back" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Right Front Side Panel" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Right Rear Side Panel" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Left Front Side Panel" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Left Rear Side Panel" to mapOf(
                            "Condition" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                            "img_urls" to listOf("")
                        ),
                    ),
                    "Test Drive Remarks" to mapOf(
                        "Engine Starts Properly" to mapOf(
                            "Status" to mapOf("Yes" to 0, "No" to 1, "With Difficulty" to 0)
                        ),
                        "Engine Health" to mapOf("Condition" to mapOf("Good" to 0, "Average" to 1, "Poor" to 0)),
                        "Gear Shifting" to mapOf(
                            "Smoothness" to mapOf(
                                "Smooth" to 0,
                                "Rough" to 0,
                                "Stuck" to 1,
                                "Jerk" to 0,
                                "late" to 0,
                            )
                        ),
                        "Turning" to mapOf("Condition" to mapOf("Normal" to 1, "Abnormal" to 0)),
                        "Suspention Check" to mapOf("Condition" to mapOf("Normal" to 1, "Abnormal" to 0)),
                        "Exhaust" to mapOf("Condition" to mapOf("Normal" to 1, "Abnormal" to 0)),
                        "Cruise Control" to mapOf(
                            "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                        ),
                        "Stearing Controls" to mapOf(
                            "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                        ),
                        "Horn" to mapOf(
                            "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                        ),
                        "Cameras" to mapOf(
                            "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                        ),
                        "Sensors" to mapOf(
                            "Condition" to mapOf("Not Available" to 0, "Working" to 1, "Not Working" to 0)
                        ),
                        "Warning Lights" to mapOf("Present" to mapOf("Yes" to 0, "No" to 1)),
                    ),
                    "Doors Check" to mapOf(
                        "Front Right Door" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                        ),
                        "Front Left Door" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                        ),
                        "Rear Right Door" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                        ),
                        "Rear Left Door" to mapOf(
                            "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                            "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0),
                        ),
                    ),
                    "Interior" to mapOf(
                        "Stearing Wheel" to mapOf(
                            "Condition" to mapOf("Damage" to 1, "Covered" to 0, "Normal" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Dashboard" to mapOf(
                            "Condition" to mapOf("Damage" to 0, "Normal" to 1, "Faded" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Front Driver Seat" to mapOf(
                            "Condition" to mapOf("Damage" to 0, "Normal" to 1),
                            "img_urls" to listOf("")
                        ),
                        "Front Passenger Seat" to mapOf(
                            "Condition" to mapOf("Damage" to 0, "Normal" to 1),
                            "img_urls" to listOf("")
                        ),
                        "Rear Seats" to mapOf(
                            "Condition" to mapOf("Damage" to 0, "Normal" to 1),
                            "img_urls" to listOf("")
                        ),
                        "Roof" to mapOf(
                            "Condition" to mapOf("Damage" to 0, "Normal" to 1, "Dirty" to 0),
                            "img_urls" to listOf("")
                        ),
                    ),
                    "Boot" to mapOf(
                        "Boot Floor" to mapOf(
                            "Condition" to mapOf("Clean" to 0, "Dirty" to 1, "Damaged" to 0),
                            "img_urls" to listOf("")
                        ),
                        "Spare Tyre" to mapOf("Status" to mapOf("Present" to 0, "Missing" to 1, "Damaged" to 0)),
                        "Tools" to mapOf(
                            "Completeness" to mapOf("Complete" to 0, "Incomplete" to 0, "Missing" to 1)
                        ),
                    ),
                )
            )
        )
}


class InspectionDataState {
    // Use mutableStateOf for single objects
    val basicInformation = mutableStateOf<BasicInformation?>(null)
    val technicalSpecifications = mutableStateOf<TechnicalSpecifications?>(null)
    val inspectionRatings = mutableStateOf<InspectionRatingsData?>(null)

    // Use mutableStateMapOf for maps
//    val bodyPartsInspection = mutableStateMapOf<String, Map<String, String>>()
//    val bodyPartsInspection = mutableStateMapOf<String, Map<String, String>>()
//    val bodyPartConditions: SnapshotStateMap<String, Map<String, String>>

    val bodyPartsInspection = mutableStateMapOf<String, Map<String, String>>()

    fun updateBodyParts(partName: String, conditions: Map<String, String>) {
        bodyPartsInspection[partName] = conditions
    }

    // Or if you want to update multiple parts at once
    fun updateAllBodyParts(newData: Map<String, Map<String, String>>) {
        bodyPartsInspection.clear()
        bodyPartsInspection.putAll(newData)
    }

    fun toCompleteInspection(): CompleteInspectionData? {
        return if (basicInformation.value != null &&
            technicalSpecifications.value != null &&
            inspectionRatings.value != null) {
            Log.d("misbah","${basicInformation.value}")
            CompleteInspectionData(
                basicInformation.value!!,
                technicalSpecifications.value!!,
                bodyPartsInspection.mapValues { it.value.toString() },
                inspectionRatings.value!!,
            )
        } else {
            null
        }
    }

//    fun updateBodyParts(partName: String, conditions: Map<String, String>) {
//        Log.d("UPDATE_BODY_PARTS", "Updating $partName with $conditions")
//        bodyPartsInspection[partName] = conditions
//        Log.d("CURRENT_BODY_PARTS", bodyPartsInspection.toString())
//    }

    // Make sure toClear is called when needed
    fun clearBodyParts() {
        bodyPartsInspection.clear()
    }

//    val bodyPartsInspection = mutableStateMapOf<String, Map<String, String>>()
//
//    fun updateBodyParts(partName: String, conditions: Map<String, String>) {
//        Log.d("UPDATE_BODY_PARTS", "Updating $partName with $conditions")
//        val existingConditions = bodyPartsInspection[partName]?.toMutableMap() ?: mutableMapOf()
//        existingConditions.putAll(conditions)
//        bodyPartsInspection[partName] = existingConditions
//        Log.d("CURRENT_BODY_PARTS", bodyPartsInspection.toString())
//    }

    fun updateBasicInformation(inputFields: Map<String, MutableState<String>>) {
        basicInformation.value = BasicInformation(
            carName = inputFields["Car Name"]?.value ?: "",
            carModel = inputFields["Car Model"]?.value ?: "",
            bodyColor = inputFields["Body color"]?.value ?: "",
            company = inputFields["Company"]?.value ?: ""
        )
    }

    fun updateTechnicalSpecifications(inputFields: Map<String, MutableState<String>>) {
        technicalSpecifications.value = TechnicalSpecifications(
            kmsDriven = inputFields["KMs Driven"]?.value ?: "",
            condition = inputFields["Condition"]?.value ?: "",
            variant = inputFields["Variant (auto/manual)"]?.value ?: "",
            fuelType = inputFields["Fuel type"]?.value ?: "",
            assembly = inputFields["Assembly"]?.value ?: "",
            engineCapacity = inputFields["Engine Capacity"]?.value ?: ""
        )
    }

    /*fun updateBodyParts(bodyParts: Map<String, String>) {
        bodyPartsInspection.clear()
        bodyParts.forEach { (key, value) ->
            bodyPartsInspection[key] = mapOf("Condition" to value)
        }
    }

//        fun updateBodyParts(bodyParts: Map<String, String>) {
//            // Convert Map<String, String> into Map<String, Map<String, String>>
//            val formatted = bodyParts.mapValues { mapOf("condition" to it.value) }
//
//            bodyPartsInspection.clear()
//            bodyPartsInspection.putAll(formatted)
//        }

//    fun updateRatings(ratings: Map<String, Float>) {
//        inspectionRatings.value = InspectionRatingsData(ratings = ratings)
//    }
//    fun updateRatings(ratings: Map<String, Float>) {
//        inspectionRatings.value = InspectionRatings(ratings = ratings)
//    }*/

}

val LocalInspectionData = compositionLocalOf { InspectionDataState() }


data class CompleteInspectionData(
    val basicInformation: BasicInformation,
    val technicalSpecifications: TechnicalSpecifications,
    val bodyPartsInspection: Map<String, String>,
    val inspectionRatings: InspectionRatingsData,
)


data class BasicInformation(
    val carName: String,
    val carModel: String,
    val bodyColor: String,
    val company: String
)

data class TechnicalSpecifications(
    val kmsDriven: String,
    val condition: String,
    val variant: String,
    val fuelType: String,
    val assembly: String,
    val engineCapacity: String
)

data class InspectionRatingsData(
    val engineCondition: Float,
    val bodyCondition: Float,
    val clutchCondition: Float,
    val steeringCondition: Float,
    val brakesCondition: Float,
    val suspensionCondition: Float,
    val acCondition: Float,
    val electricSystemCondition: Float,
    val averageRating: Float
)

data class BodyPartsInspectionData(
    val parts: Map<String, Map<String, String>>,
    val images: Map<String, List<String>>
)