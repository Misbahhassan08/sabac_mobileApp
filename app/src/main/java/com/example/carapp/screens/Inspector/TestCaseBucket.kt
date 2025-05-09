package com.example.carapp.screens.Inspector


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.carapp.R
import com.example.carapp.assets.AssetHelper
import com.example.carapp.models.InspectionModel
import com.example.carapp.screens.AddImage
import com.example.carapp.screens.cardColor
import com.example.carapp.screens.getToken
import com.example.carapp.screens.redcolor
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.window.Dialog
import com.google.gson.JsonObject
import java.io.FileOutputStream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*
@Composable
fun CarInspectionUI() {
    val carInspectionData = remember {
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
                        //Body Frame***************
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
                        // *** TEST DRIVE REMARKS *** //
                        "Test Drive Remarks" to mapOf(
                            "Engine Starts Properly" to mapOf(
                                "Status" to mapOf("Yes" to 0, "No" to 1, "With Difficulty" to 0)
                            ),
                            "Engine Health" to mapOf(
                                "Condition" to mapOf("Good" to 0, "Average" to 1, "Poor" to 0)
                            ),
                            "Gear Shifting" to mapOf(
                                "Smoothness" to mapOf("Good" to 0, "Rough" to 1, "Stuck" to 0, "Jerk" to 0, "late" to 0)
                            ),
                            "Turning" to mapOf(
                                "Condition" to mapOf("Normal" to 0, "Abnormal" to 1,)
                            ),
                            "Suspention Check" to mapOf(
                                "Condition" to mapOf("Normal" to 0, "Abnormal" to 1,)
                            ),
                            "Exhaust" to mapOf(
                                "Condition" to mapOf("Normal" to 0, "Abnormal" to 1,)
                            ),
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
                            "Warning Lights" to mapOf(
                                "Present" to mapOf("Yes" to 0, "No" to 1,)
                            ),

                            //*** Doors Check **** //
                        ),
                        "Doors Check" to mapOf(
                            "Front Right Door" to mapOf(
                                "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                                "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0)
                            ),
                            "Front Left Door" to mapOf(
                                "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                                "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0)
                            ),
                            "Rear Right Door" to mapOf(
                                "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                                "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0)
                            ),
                            "Rear Left Door" to mapOf(
                                "Paint" to mapOf("Original" to 0, "Repainted" to 1),
                                "Seals" to mapOf("Ok" to 0, "Damaged" to 1, "Repaired" to 0)
                            ),
                        ),
                        //*** Interior **** //
                        "Interior" to mapOf(
                            "Stearing Wheel" to mapOf(
                                "Condition" to mapOf("Damage" to 1, "Covered" to 0, "Normal" to 0),
                                "img_urls" to listOf("")
                            ),
                            "Dashboard" to mapOf(
                                "Condition" to mapOf("Damage" to 1, "Faded" to 0, "Normal" to 0),
                                "img_urls" to listOf("")
                            ),
                            "Front Driver Seat" to mapOf(
                                "Condition" to mapOf("Damage" to 1, "Normal" to 0),
                                "img_urls" to listOf("")
                            ),
                            "Front Passenger Seat" to mapOf(
                                "Condition" to mapOf("Damage" to 1, "Normal" to 0),
                                "img_urls" to listOf("")
                            ),
                            "Rear Seats" to mapOf(
                                "Condition" to mapOf("Damage" to 1, "Normal" to 0),
                                "img_urls" to listOf("")
                            ),
                            "Roof" to mapOf(
                                "Condition" to mapOf("Damage" to 1, "Dirty" to 0, "Normal" to 0),
                                "img_urls" to listOf("")
                            ),
                        ),

                        //*** Boot **** //
                        "Boot" to mapOf(
                            "Boot Floor" to mapOf(
                                "Condition" to mapOf("Clean" to 0, "Dirty" to 1, "Damaged" to 0),
                                "img_urls" to listOf("")
                            ),
                            "Spare Tyre" to mapOf(
                                "Status" to mapOf("Present" to 0, "Missing" to 1, "Damaged" to 0),
                            ),
                            "Tools" to mapOf(
                                "Completeness" to mapOf("Complete" to 0, "Incomplete" to 1, "Missing" to 0),
                            ),
                        )
                    )
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Car Inspection Report", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Start with the top-level "Car Detail"
        carInspectionData["Car Detail"]?.let { carDetail ->
            ExpandableCard(title = "Car Detail") {
                carDetail.forEach { (sectionName, sectionData) ->
                    ExpandableCard(title = sectionName) {
                        when (sectionName) {
                            "Body Parts Inspection" -> {
                                sectionData.forEach { (partCategory, partData) ->
                                    ExpandableCard(title = partCategory) {
                                        partData.forEach { (partName, partDetails) ->
                                            if (partDetails is Map<*, *>) {
                                                ExpandableCard(title = partName) {
                                                    partDetails.forEach { (detailName, detailValue) ->
                                                        when (detailValue) {
                                                            is Map<*, *> -> {
                                                                // For nested maps (like Paint, Seals, etc.)
                                                                Text(
                                                                    text = "$detailName: ${detailValue.entries.joinToString { "${it.key} (${it.value})" }}",
                                                                    modifier = Modifier.padding(8.dp)
                                                                )
                                                            }
                                                            is List<*> -> {
                                                                // For lists (like img_urls)
                                                                Text(
                                                                    text = "$detailName: ${detailValue.joinToString()}",
                                                                    modifier = Modifier.padding(8.dp)
                                                                )
                                                            }
                                                            else -> {
                                                                Text(
                                                                    text = "$detailName: $detailValue",
                                                                    modifier = Modifier.padding(8.dp)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                Text(
                                                    text = "$partName: $partDetails",
                                                    modifier = Modifier.padding(8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            else -> {
                                // Handle other sections if needed
                                Text("Other section: $sectionName")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}

*/

 */


 */
 */

@Composable
fun CarInspectionUI() {
    val carInspectionData = remember {
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

    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Car Inspection Report", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Main "Car Detail" card
            var carDetailExpanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Car Detail",
                            style = MaterialTheme.typography.titleMedium
                        )
                        IconButton(
                            onClick = { carDetailExpanded = !carDetailExpanded },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (carDetailExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (carDetailExpanded) "Collapse" else "Expand"
                            )
                        }
                    }

                    if (carDetailExpanded) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // "Body Parts Inspection" sub-card
                        var bodyPartsExpanded by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Body Parts Inspection",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    IconButton(
                                        onClick = { bodyPartsExpanded = !bodyPartsExpanded },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (bodyPartsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = if (bodyPartsExpanded) "Collapse" else "Expand"
                                        )
                                    }
                                }

                                if (bodyPartsExpanded) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    carInspectionData["Car Detail"]?.get("Body Parts Inspection")?.let { bodyParts ->
                                        Column {
                                            bodyParts.forEach { (partCategory, partData) ->
                                                Spacer(modifier = Modifier.height(8.dp))
                                                ExpandableCard(
                                                    title = partCategory,
                                                    modifier = Modifier.padding(bottom = 8.dp)
                                                ) {
                                                    Column {
                                                        partData.forEach { (partName, partDetails) ->
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            if (partDetails is Map<*, *>) {
                                                                ExpandableCard(
                                                                    title = partName,
                                                                    modifier = Modifier.padding(bottom = 8.dp)
                                                                ) {
                                                                    Column {
                                                                        partDetails.forEach { (detailName, detailValue) ->
                                                                            Spacer(modifier = Modifier.height(4.dp))
                                                                            when (detailValue) {
                                                                                is Map<*, *> -> {
                                                                                    Text(
                                                                                        text = "$detailName: ${detailValue.entries.joinToString { "${it.key} (${it.value})" }}",
                                                                                        modifier = Modifier.padding(8.dp)
                                                                                    )
                                                                                }
                                                                                is List<*> -> {
                                                                                    Text(
                                                                                        text = "$detailName: ${detailValue.joinToString()}",
                                                                                        modifier = Modifier.padding(8.dp)
                                                                                    )
                                                                                }
                                                                                else -> {
                                                                                    Text(
                                                                                        text = "$detailName: $detailValue",
                                                                                        modifier = Modifier.padding(8.dp)
                                                                                    )
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                Text(
                                                                    text = "$partName: $partDetails",
                                                                    modifier = Modifier.padding(8.dp)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}*/

    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogOptions by remember { mutableStateOf<Map<String, Map<String, Int>>>(emptyMap()) }
    val selectedOptions = remember { mutableStateMapOf<String, Map<String, String>>() }
    var currentPartName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Car Inspection Report", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Main scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Main "Car Detail" card
            var carDetailExpanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column(modifier = Modifier
                    .clickable { carDetailExpanded = !carDetailExpanded }
                    .padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Car Detail",
                            style = MaterialTheme.typography.titleMedium
                        )
                        IconButton(
                            onClick = { carDetailExpanded = !carDetailExpanded },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (carDetailExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (carDetailExpanded) "Collapse" else "Expand"
                            )
                        }
                    }

                    if (carDetailExpanded) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // "Body Parts Inspection" sub-card
                        var bodyPartsExpanded by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier
                                .clickable { bodyPartsExpanded = !bodyPartsExpanded }
                                .padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Body Parts Inspection",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    IconButton(
                                        onClick = { bodyPartsExpanded = !bodyPartsExpanded },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (bodyPartsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                            contentDescription = if (bodyPartsExpanded) "Collapse" else "Expand"
                                        )
                                    }
                                }

                                if (bodyPartsExpanded) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    carInspectionData["Car Detail"]?.get("Body Parts Inspection")?.let { bodyParts ->
                                        Column {
                                            bodyParts.forEach { (partCategory, partData) ->
                                                Spacer(modifier = Modifier.height(8.dp))
                                                ExpandableCard(
                                                    title = partCategory,
                                                    modifier = Modifier.padding(bottom = 8.dp),
                                                ){
                                                    Column {
                                                        partData.forEach { (partName, partDetails) ->
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            InspectionItemCard(
                                                                title = partName,
                                                                onClick = {
                                                                    dialogTitle = partName
                                                                    currentPartName = partName // Store the current part name
                                                                    if (partDetails is Map<*, *>) {
                                                                        val optionsMap = mutableMapOf<String, Map<String, Int>>()

                                                                        partDetails.forEach { (detailName, detailValue) ->
                                                                            if (detailValue is Map<*, *>) {
                                                                                val options = detailValue.mapKeys { it.key.toString() }
                                                                                    .mapValues { it.value.toString().toInt() }
                                                                                optionsMap[detailName.toString()] = options
                                                                            }
                                                                        }

                                                                        dialogOptions = optionsMap

                                                                        // Initialize with default selections if not already set
                                                                        if (!selectedOptions.containsKey(partName)) {
                                                                            val initialSelections = mutableMapOf<String, String>()
                                                                            optionsMap.forEach { (category, options) ->
                                                                                initialSelections[category] = options.entries.firstOrNull { it.value == 1 }?.key ?: ""
                                                                            }
                                                                            selectedOptions[partName] = initialSelections
                                                                        }
                                                                    }
                                                                    showDialog = true
                                                                }
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(dialogTitle) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    dialogOptions.forEach { (category, options) ->
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )

                        options.forEach { (option, _) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedOptions[currentPartName] =
                                            selectedOptions[currentPartName]?.toMutableMap()?.apply {
                                                put(category, option)
                                            } ?: mutableMapOf(category to option)
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = selectedOptions[currentPartName]?.get(category) == option,
                                    onClick = {
                                        selectedOptions[currentPartName] =
                                            selectedOptions[currentPartName]?.toMutableMap()?.apply {
                                                put(category, option)
                                            } ?: mutableMapOf(category to option)
                                    }
                                )
                                Text(
                                    text = option,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Save the selected options
                        showDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }*/

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(dialogTitle) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    // Check if this part has image URLs
                    val imageUrls = remember(currentPartName) {
                        // Get all body parts
                        val bodyParts = (carInspectionData["Car Detail"] as? Map<String, *>)?.get("Body Parts Inspection") as? Map<String, *>

                        // Find the specific part data
                        bodyParts?.values?.mapNotNull { partData ->
                            (partData as? Map<String, *>)?.entries?.firstOrNull { (key, _) ->
                                key == currentPartName
                            }?.let { (_, details) ->
                                (details as? Map<String, *>)?.get("img_urls") as? List<String>
                            }
                        }?.firstOrNull { !it.isNullOrEmpty() } ?: emptyList()
                    }

// Only show ImageUploadCard if there are image URLs or the part can have images
                    val shouldShowImageCard = imageUrls.isNotEmpty() ||
                            (currentPartName in listOf("Bonnet", "Front Driver Door", "Front Passenger Door",
                                "Rear Right Door", "Rear Right Fender", "Rear Left Fender",
                                "Stearing Wheel", "Dashboard", "Front Driver Seat",
                                "Front Passenger Seat", "Rear Seats", "Roof",
                                "Boot Floor"))

                    if (shouldShowImageCard) {
                        ImageUploadCard(imageUrls = imageUrls)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
//                    if (imageUrls != null) {
//                        ImageUploadCard(imageUrls = imageUrls)
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }

                    dialogOptions.forEach { (category, options) ->
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )

                        options.forEach { (option, _) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedOptions[currentPartName] =
                                            selectedOptions[currentPartName]?.toMutableMap()?.apply {
                                                put(category, option)
                                            } ?: mutableMapOf(category to option)
                                    }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = selectedOptions[currentPartName]?.get(category) == option,
                                    onClick = {
                                        selectedOptions[currentPartName] =
                                            selectedOptions[currentPartName]?.toMutableMap()?.apply {
                                                put(category, option)
                                            } ?: mutableMapOf(category to option)
                                    }
                                )
                                Text(
                                    text = option,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun InspectionItemCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            Color(0xFFF5F5F5),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "View details"
            )
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}

@Composable
fun ImageUploadCard(imageUrls: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Images",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (imageUrls.isNotEmpty() && imageUrls.first().isNotBlank()) {
                // Display existing images
                LazyRow {
                    items(imageUrls) { url ->
                        if (url.isNotBlank()) {
                            Image(
                                painter = rememberAsyncImagePainter(url),
                                contentDescription = "Inspection image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
            } else {
                // Show upload option if no images
                Button(
                    onClick = { /* Handle image upload */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Image")
                }
            }
        }
    }
}

// First, modify the ImageUploadCard composable to be expandable
@Composable
fun ExpandableImageUploadCard(imageUrls: List<String>) {
    var expanded by remember { mutableStateOf(true) }
    val selectedImagesCard1 = remember { mutableStateListOf<String>() }
    val imagesError = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Images",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (expanded) {
                /*Spacer(modifier = Modifier.height(8.dp))
                if (imageUrls.isNotEmpty() && imageUrls.first().isNotBlank()) {
                    LazyRow {
                        items(imageUrls) { url ->
                            if (url.isNotBlank()) {
                                Image(
                                    painter = rememberAsyncImagePainter(url),
                                    contentDescription = "Inspection image",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(end = 8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                        }
                    }
                } else {
                    Button(
                        onClick = {  },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Image")
                    }
                }*/
                Inspectiod(
                    selectedImages = selectedImagesCard1,
                    showError = imagesError.value,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    cardName = "Test Case"
                )
            }
        }
    }
}



