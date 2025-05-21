package com.example.carapp.screens.Inspector



import android.app.Activity
import android.content.Context
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.carapp.Apis.TestApi
import com.example.carapp.R
import com.example.carapp.models.CarI
import com.example.carapp.models.CarListViewModelI
import com.example.carapp.models.DateWithSlots
import com.example.carapp.models.InspectionItem
import com.example.carapp.screens.DatePickerModal
import com.example.carapp.screens.SimpleTimeMaker
import com.example.carapp.screens.convertMillisToDate
import com.example.carapp.screens.getToken
import com.example.carapp.viewmodels.InspectionViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.FilterChip
import com.example.carapp.assets.redcolor


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTimeSlotScreen(navController: NavController) {
    val inspectionViewModel: InspectionViewModel = hiltViewModel()
    val response by inspectionViewModel.response.collectAsState()

    val (isOpenSlot, setIsOpenSlot) = remember { mutableStateOf(false) }
    val (isOpenInspections, setIsOpenInspections) = remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()

    systemUiController.isStatusBarVisible = false

    Scaffold(
        topBar = {
            TopAppBar(
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
                .background(
                    MaterialTheme.colorScheme.background
                ),
        ) {
            Spacer(Modifier.height(16.dp))
            MakeHorizontalBar("Make Appointment Slots", isOpenSlot, setIsOpenSlot) {
                AppointmentsBox(navController,inspectionViewModel)
            }
//            Spacer(Modifier.height(16.dp))
//            MakeHorizontalBar("Upcoming Inspections", isOpenInspections, setIsOpenInspections) {
//                UpcomingInspectionsList(inspectionViewModel)
//            }
        }
    }
}

@Composable
fun UpcomingInspectionsList(inspectionViewModel: InspectionViewModel) {
    val todayInspections = listOf(
        InspectionItem(
            carName = "Toyota Corolla",
            time = "10:00 AM",
            location = "Service Center A",
            onClick = { println("Clicked Toyota Corolla") }
        ),
        InspectionItem(
            carName = "Honda Civic",
            time = "11:30 AM",
            location = "Service Center B",
            onClick = { println("Clicked Honda Civic") }
        )
    )
    val tomorrowInspections = listOf(
        InspectionItem(
            carName = "Ford Mustang",
            time = "02:00 PM",
            location = "Service Center C",
            onClick = { println("Clicked Ford Mustang") }
        )
    )

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        SectionWithInspections(
            title = "Today",
            inspections = todayInspections,
            emptyMessage = "No inspections scheduled for today."
        )
        Spacer(modifier = Modifier.height(16.dp))
        SectionWithInspections(
            title = "Tomorrow",
            inspections = tomorrowInspections,
            emptyMessage = "No inspections scheduled for tomorrow."
        )
    }
}

@Composable
fun SectionWithInspections(title: String, inspections: List<InspectionItem>, emptyMessage: String) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = "Toggle $title",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        if (isExpanded) {
            if (inspections.isNotEmpty()) {
                InspectionSectionList(inspections)
            } else {
                Text(
                    text = emptyMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun InspectionSectionList(inspections: List<InspectionItem>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(inspections) { inspection ->
            InspectionCard(inspection)
        }
    }
}

@Composable
fun InspectionCard(inspection: InspectionItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { inspection.onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Car",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Car: ${inspection.carName}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Time: ${inspection.time}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Location: ${inspection.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Details",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun MakeHorizontalBar(label: String,
                      isOpen: Boolean,
                      setIsOpen: (Boolean) -> Unit,
                      item: @Composable (inspectionViewModel: InspectionViewModel) -> Unit)
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp,
                redcolor,
                RoundedCornerShape(8.dp))
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    redcolor
//                    brush = Brush.linearGradient(
//                        colors = listOf(
//                            Color(0xFF42A5F5),
//                            Color(0xFF0D47A1),
//                            Color(0xFF0D47A1),
//
//                        )
//                    )
                    , RoundedCornerShape(8.dp))
                .clickable { setIsOpen(!isOpen) }
                .padding(horizontal = 4.dp, vertical = 2.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = label, color = Color.White)
                IconButton(onClick = { setIsOpen(!isOpen) }) {
                    Icon(if (isOpen) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp, contentDescription = "Open", tint = Color.White)
                }
            }
        }
        if(isOpen)  {
            item(inspectionViewModel = hiltViewModel())
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentsBox(navController: NavController, inspectionViewModel: InspectionViewModel) {
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
    val context = LocalContext.current

    Button(
        onClick = { showDateDialog = true },
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .clip(RoundedCornerShape(16.dp))
            .background(
                redcolor
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF0D47A1),
//                        Color(0xFF0D47A1),
//                        Color(0xFF42A5F5)
//                    )
//                )
            ),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
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
            onClick = {
                val jsonBody = createJsonBody(dateWithSlotList)
                postAppointmentSlots(navController, context, jsonBody)            },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    redcolor
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            Color(0xFF0D47A1),
//                            Color(0xFF0D47A1),
//                            Color(0xFF42A5F5)
//                        )
//                    )
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
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
@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
/*Changed-UI
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppointmentsBox(navController: NavController, inspectionViewModel: InspectionViewModel) {
    val dateWithSlotList = remember { mutableStateListOf<DateWithSlots>() }
    var showDateDialog by remember { mutableStateOf(false) }
    var showSlotMaker by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Header with description
        Text(
            text = "Schedule Inspection Appointments",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create available time slots for sellers to book inspections",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Add Date Button
        ElevatedButton(
            onClick = { showDateDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = redcolor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add Date",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Appointment Date", fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Date Picker Dialog
        if (showDateDialog) {
            DatePickerModal(
                onDateSelected = {
                    dateWithSlotList.add(DateWithSlots(it, mutableListOf()))
                    showDateDialog = false
                },
                onDismiss = { showDateDialog = false },
                hiddenDatesList = dateWithSlotList.map { it.date }
            )
        }

        // Dates List
        if (dateWithSlotList.isNotEmpty()) {
            Text(
                text = "Your Scheduled Dates",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(dateWithSlotList) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = formatDate(item.date),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                IconButton(
                                    onClick = { dateWithSlotList.removeAt(index) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete date",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (item.timeSlots.isEmpty()) {
                                Text(
                                    text = "No time slots added",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            } else {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    item.timeSlots.forEachIndexed { slotIndex, slot ->
                                        FilterChip(
                                            selected = false,
                                            onClick = {
                                                val updatedSlots = item.timeSlots.toMutableList()
                                                updatedSlots.removeAt(slotIndex)
                                                dateWithSlotList[index] = item.copy(timeSlots = updatedSlots)
                                            },
                                            label = { Text(slot) },
                                            enabled = true
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedButton(
                                onClick = {
                                    showSlotMaker = true
                                    selectedIndex = index
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add time slot",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add Time Slot")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            FilledTonalButton(
                onClick = {
                    val jsonBody = createJsonBody(dateWithSlotList)
                    postAppointmentSlots(navController, context, jsonBody)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = redcolor,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    "Publish Appointment Slots",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tip: Tap on any date or time slot to delete it",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // Time Slot Maker Dialog
        if (showSlotMaker) {
            SimpleTimeMaker(
                onDismiss = { showSlotMaker = false },
                onConfirm = { time ->
                    val updatedSlots = dateWithSlotList[selectedIndex].timeSlots.toMutableList()
                    updatedSlots.add(time)
                    dateWithSlotList[selectedIndex] = dateWithSlotList[selectedIndex].copy(timeSlots = updatedSlots)
                    showSlotMaker = false
                    selectedIndex = -1
                }
            )
        }
    }
}
*/

@Composable
fun DateWithSlotsItem(
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
            color = redcolor,
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


fun createJsonBody(dateWithSlotList: List<DateWithSlots>): String {
    val jsonArray = JSONArray()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    for (item in dateWithSlotList) {
        val dateObject = JSONObject()
        val formattedDate = dateFormat.format(item.date?.let { Date(it) })

        dateObject.put("date", formattedDate)

        val slotsArray = JSONArray()
        for (slot in item.timeSlots) {
            slotsArray.put(slot.trim())
        }

        dateObject.put("slots", slotsArray)

        jsonArray.put(dateObject)
    }

    val jsonObject = JSONObject()
    jsonObject.put("dateSlots", jsonArray)

    return jsonObject.toString()
}


/*
fun postAppointmentSlots(jsonBody: String) {
    val client = OkHttpClient()

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val requestBody = jsonBody.toRequestBody(mediaType)

    val request = Request.Builder()
        .url(TestApi.add_availability)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("PostError", "Failed to send request: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("PostSuccess", "Successfully posted appointment slots")
            } else {
                Log.e("PostError", "Server error: ${response.code}")
            }
        }
    })
}
*/



fun postAppointmentSlots(navController: NavController, context: Context, jsonBody: String) {
    val client = OkHttpClient()
    val token = getToken(context)

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val requestBody = jsonBody.toRequestBody(mediaType)

    val requestBuilder = Request.Builder()
        .url(TestApi.add_availability)
        .post(requestBody)

    token?.let {
        requestBuilder.addHeader("Authorization", "Bearer $it")
    }

    val request = requestBuilder.build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("PostError", "Failed to send request: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("PostSuccess", "Successfully posted appointment slots")
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Appointment Slots Added Successfully!", Toast.LENGTH_SHORT).show()
                }
                (context as? Activity)?.runOnUiThread {
                    navController.navigate("done")
                }
            } else {
                Log.e("PostError", "Server error: ${response.code}")
            }
        }
    })
}
