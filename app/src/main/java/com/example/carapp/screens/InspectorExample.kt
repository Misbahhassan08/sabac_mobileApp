package com.example.carapp.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.carapp.models.DateWithSlots
import com.example.carapp.screens.Inspector.DateWithSlotsItem
import com.example.carapp.viewmodels.InspectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InspectorScreenExample() {
    val inspectionViewModel: InspectionViewModel = hiltViewModel()
    val response by inspectionViewModel.response.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
//                backgroundColor = Color(0xFF282931),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF282931)),
                title = {
                    Text(
                        text = "Inspection",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Make appointment dates available to sellers for inspection",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                val dateWithSlotList = remember { mutableStateListOf<DateWithSlots>() }
                var showDateDialog by remember { mutableStateOf(false) }
                var showSlotMaker by remember { mutableStateOf(false) }
                var selectedIndex by remember { mutableStateOf(-1) }

                Button(
                    onClick = { showDateDialog = true },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Date", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add Date", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (showDateDialog) {
                    DatePickerModal(
                        onDateSelected = {
                            dateWithSlotList.add(DateWithSlots(it, mutableListOf()))
                            Log.d("Dates Added", dateWithSlotList.toString())
                            showDateDialog = false
                        },
                        onDismiss = { showDateDialog = false },
                        hiddenDatesList = dateWithSlotList.map { it.date }
                    )
                }

                if (dateWithSlotList.isNotEmpty()) {
                    Text(text = "Selected Dates:", style = MaterialTheme.typography.headlineSmall)

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(dateWithSlotList) { index, item ->
                            DateWithSlotsItem(
                                dateWithSlots = item,
                                onDeleteDate = { dateWithSlotList.removeAt(index) },
                                onAddTimeSlot = {
                                    showSlotMaker = true
                                    selectedIndex = index
                                },
                                onDeleteSlot = { slotIndex ->
                                    val updatedSlots = item.timeSlots.toMutableList()
                                    updatedSlots.removeAt(slotIndex)
                                    dateWithSlotList[index] = item.copy(timeSlots = updatedSlots)
                                }
                            )
                        }
                    }

                    if (showSlotMaker) {
                        SimpleTimeMaker(
                            onDismiss = { showSlotMaker = false },
                            onConfirm = {
                                val updatedSlots = dateWithSlotList[selectedIndex].timeSlots.toMutableList()
                                updatedSlots.add(it)
                                dateWithSlotList[selectedIndex] = dateWithSlotList[selectedIndex].copy(timeSlots = updatedSlots)
                                showSlotMaker = false
                                selectedIndex = -1
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { inspectionViewModel.submitSlots(dateWithSlotList) },
                        modifier = Modifier.fillMaxWidth(0.6f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Post", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "To delete a date or time, tap on it",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun DateWithSlotsItemExp(
    dateWithSlots: DateWithSlots,
    onDeleteDate: () -> Unit,
    onAddTimeSlot: () -> Unit,
    onDeleteSlot: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = convertMillisToDate(dateWithSlots.date ?: 0),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.clickable { onDeleteDate() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add Time Slots",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onAddTimeSlot() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        dateWithSlots.timeSlots.forEachIndexed { index, slot ->
            Text(
                text = slot,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable { onDeleteSlot(index) }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewSc() {
    InspectorScreenExample()
}
