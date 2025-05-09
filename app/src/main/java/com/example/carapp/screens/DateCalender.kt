package com.example.carapp.screens

import android.os.Build
import android.util.Log
import androidx.activity.ComponentDialog
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.carapp.R
import kotlinx.coroutines.launch
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalenderDate() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Inspection Date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("DOB") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
//            .height(44.dp)
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
//        DatePickerModal(
//            onDateSelected = { selectedDate = it },
//            onDismiss = { showModal = false }
//        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    hiddenDatesList: List<Long?> = emptyList()
) {
    val today = LocalDate.now()
    val datePickerState = rememberDatePickerState(
        selectableDates = object  : SelectableDates {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                return (date.isEqual(today) || date.isAfter(today)) && !hiddenDatesList.contains(utcTimeMillis)
            }
            override fun isSelectableYear(year: Int): Boolean {
                return true
            }
        }
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
//                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String{
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, widthDp = 309, heightDp = 675)
//@Composable
//fun CalenderPreview() {
//    var selectedDate by remember { mutableStateOf<Long?>(null) }
//    var time by remember { mutableStateOf<String?>("") }
//    var showModal by remember { mutableStateOf(false) }
//    DatePickerModal(
//        onDateSelected = { selectedDate = it },
//        onDismiss = { showModal = false }
//    )
//}

@Composable
fun TimeSlotPicker() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedTimeSlot by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Pick Time Slot")
        }

        if (selectedTimeSlot.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Selected Time Slot: $selectedTimeSlot")
        }

        if (showDialog) {
            TimeSlotDialog(
                timeSlots = generateTimeSlots(),
                onConfirm = {
                    selectedTimeSlot = it
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun TimeSlotDialog(
    timeSlots: List<String>,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = { onConfirm(timeSlots[selectedIndex]) }) {
                Text("Confirm")
            }
        },
        text = {
            GetTime(timeSlots)
        }
    )
}

@Composable
fun GetTime(
    timeSlots: List<String>,
//    selectedIndex: Int
//    onDismiss: () -> Unit
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val (selectedIndex, setSelection) = remember { mutableStateOf(0) }

//    LaunchedEffect(Unit) {
//        listState.scrollToItem(selectedIndex)
//    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Select a Time Slot",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )

        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(timeSlots) { index, timeSlot ->
                    Text(
                        text = timeSlot,
                        textAlign = TextAlign.Center,
                        fontSize = if (index == selectedIndex) 18.sp else 14.sp,
                        color = if (index == selectedIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(40.dp)
                            .clickable {
                                setSelection(index)
                                coroutineScope.launch {
                                    listState.animateScrollToItem(index)
                                }
                            }
                    )
                }
            }
        }
    }


    // Update selected index as user scrolls
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
            setSelection(index)
        }
//        snapshotFlow {
//            val centerIndex =
//                (listState.firstVisibleItemIndex + (listState.layoutInfo.visibleItemsInfo.size / 2.0)).roundToInt()
//            centerIndex.coerceIn(0, timeSlots.lastIndex
//            )
//        }.collect { index ->
//            setSelection(index)
//        }
    }

    // Snap to the center when user stops scrolling
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }.collect { isScrolling ->
            if (!isScrolling) {
                coroutineScope.launch {
                    listState.animateScrollToItem(selectedIndex)
                }
            }
        }
    }
}

fun generateTimeSlots(): List<String> {
    return listOf(
        "09:00 to 10:00", "10:00 to 11:00", "11:00 to 12:00",
        "12:00 to 01:00", "01:00 to 02:00", "02:00 to 03:00",
        "03:00 to 04:00", "04:00 to 05:00"
    )
}

@Composable
fun GetTimeSlotListDialog(timeSlots: List<String> = generateTimeSlots()) {
    var (selectedSlot, onSelectedSlot) = remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            showDialog = true
        }) {
            Text("Open Time Slot Dialog")
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false },
            ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Pick a Time Slot")
                        Box(
                            modifier = Modifier
//                                .height(200.dp)
                                .fillMaxWidth()
                        ) {
                            InputFieldWithDropdown(
                                "Select",
                                R.drawable.tag_icon,
                                timeSlots,
                                selectedSlot,
                                onSelectedSlot
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeSlotSelectionDialog() {
    // State variables
    var showDialog by remember { mutableStateOf(false) }
    var showTimeSlotDropdown by remember { mutableStateOf(false) }
    var selectedTimeSlot by remember { mutableStateOf("Select Time Slot") }

    // Time slot options
    val timeSlots = listOf(
        "09:00 AM", "10:00 AM", "11:00 AM",
        "12:00 PM", "01:00 PM", "02:00 PM",
        "03:00 PM", "04:00 PM", "05:00 PM"
    )

    // Main button to open dialog
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Open Time Slot Selection")
        }

        // Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Select Time Slot") },
                text = {
                    Column {
                        // Text field that opens dropdown when clicked
                        ExposedDropdownMenuBox(
                            expanded = showTimeSlotDropdown,
                            onExpandedChange = {
                                showTimeSlotDropdown = !showTimeSlotDropdown
                            }
                        ) {
                            // Actual text field
                            TextField(
                                value = selectedTimeSlot,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Choose Time Slot") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = showTimeSlotDropdown
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                modifier = Modifier.menuAnchor()
                            )

                            // Dropdown menu
                            ExposedDropdownMenu(
                                expanded = showTimeSlotDropdown,
                                onDismissRequest = {
                                    showTimeSlotDropdown = false
                                },
                                modifier = Modifier.height(200.dp),
//                                scrollState = remember { ScrollableState }
                            ) {
                                timeSlots.forEach { timeSlot ->
                                    DropdownMenuItem(
                                        text = { Text(timeSlot) },
                                        onClick = {
                                            selectedTimeSlot = timeSlot
                                            showTimeSlotDropdown = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Handle confirmation logic here
                            showDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}


@Composable
fun OpenSimpleTimeSlotListDialog(
    timeSlots: List<String> = generateTimeSlots(),
) {
    var showDialog by remember { mutableStateOf(false) }
    var select by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Open Time Slot Dialogn")
        }
        if(showDialog) {
            SimpleTimeSlotListDialog(
                onDismiss = { showDialog = false },
                onConfirm = {select = it},
                timeSlots = timeSlots
            )

        }
    }
}

@Composable
fun SimpleTimeSlotListDialog(
    onDismiss: () -> Unit = { },
    onConfirm: (String) -> Unit,
    timeSlots: List<String>){
    val (select, setSelection) = remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
//                .height(380.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(12.dp),
            ) {
                Text(text = "Choose a time slot",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center)
                if(select.isNotEmpty()){
                    Text(text = "Tap on Confirm to book your slot of $select",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth())
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    timeSlots.forEach { slot ->
                        item{
                            Text(text = slot,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .clickable {
                                        setSelection(slot)
                                    },
                                fontWeight = if (select == slot) FontWeight.Bold else FontWeight.Normal,
                                color = if (select == slot) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {onConfirm(select)}) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun MakeTime(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var showDialog by remember { mutableStateOf(true) }
        Button(onClick = {showDialog = true}) {
            Text("Make Time")
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = onDismiss
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Make a time slot",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                    TimeScroll()
                }
            }
        }
    }
}

@Composable
fun TimeScroll() {
    val list = List(10) { "$it" }
    val pagerState = rememberPagerState{ list.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .width(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x26000000))
                .fillMaxWidth()
        ) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "", tint = Color.White)
        }
        VerticalPager(
            state = pagerState,
//            pageSize = PageSize.Fixed(30.dp),
            modifier = Modifier.height(86.dp),
//            pageSpacing = 16.dp,
            userScrollEnabled = false
        ) { page ->
//            Text(list[page - 1])
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                if(page != 0) {
                    Text(list[page - 1], fontSize = 16.sp)
                }
                Text(list[page], fontSize = 22.sp)
                if(page != list.size - 1) {
                    Text(list[page + 1], fontSize = 16.sp)
                }
            }
            Log.d("PageNumber", page.toString())
        }
        IconButton(onClick = {

            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x26000000))
                .fillMaxWidth()
        ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "", tint = Color.White)
        }
    }
}


/*
@Composable
fun SimpleTimeMaker(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val (sHrs, setSHrs) = remember { mutableStateOf("") }
    val (sMin, setSMin) = remember { mutableStateOf("") }
    val (eHrs, setEHrs) = remember { mutableStateOf("") }
    val (eMin, setEMin) = remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        var showDialog by remember { mutableStateOf(true) }
//        Button(onClick = {showDialog = true}) {
//            Text("Make Time")
//        }
//        if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(text = "Make a time slot",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GetHHMM(modifier = Modifier.weight(1f),
                            mins = sMin, setMins = setSMin,
                            hrs = sHrs, setHrs = setSHrs)
//                        Text(text = "to",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(12.dp))
//                        GetHHMM(modifier = Modifier.weight(1f),
//                            mins = eMin, setMins = setEMin,
//                            hrs = eHrs, setHrs = setEHrs)
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = {
                            onConfirm("${checkNum(sHrs)}:${checkNum(sMin)} "
//                                    + "to ${checkNum(eHrs)}:${checkNum(eMin)}"
                            )
                        }) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
//        }
    }
}
*/

/*
@Composable
fun SimpleTimeMaker(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val (sHrs, setSHrs) = remember { mutableStateOf("") }
    val (sMin, setSMin) = remember { mutableStateOf("") }
    val (eHrs, setEHrs) = remember { mutableStateOf("") }
    val (eMin, setEMin) = remember { mutableStateOf("") }

//    val (sPeriod, setSPeriod) = remember { mutableStateOf("AM") }
//
//    val (selectedPeriod, setSelectedPeriod) = remember { mutableStateOf("AM") }
//    PeriodDropdown(selectedPeriod, setSelectedPeriod)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Make a time slot",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        PeriodDropdown(sPeriod, setSPeriod) // AM/PM before hours
                        GetHHMM(
                            modifier = Modifier.weight(1f),
                            mins = sMin, setMins = setSMin,
                            hrs = sHrs, setHrs = setSHrs
                        )
//                        Text(text = "to",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(12.dp))
//                        GetHHMM(modifier = Modifier.weight(1f),
//                            mins = eMin, setMins = setEMin,
//                            hrs = eHrs, setHrs = setEHrs)
//                        PeriodDropdown(ePeriod, setEPeriod) // AM/PM before minutes
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = {
                            onConfirm("${checkNum(sHrs)}:${checkNum(sMin)} "
//                                    + "to ${checkNum(eHrs)}:${checkNum(eMin)}"
                            )
                        }) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}

*/
@Composable
fun SimpleTimeMaker(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val hours = (1..12).toList().map { it.toString().padStart(2, '0') }
    val minutes = (0..59).toList().map { it.toString().padStart(2, '0') }
    val periods = listOf("AM", "PM")

    var selectedHour by remember { mutableStateOf(hours[0]) }
    var selectedMinute by remember { mutableStateOf(minutes[0]) }
    var selectedPeriod by remember { mutableStateOf(periods[0]) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberPicker(
                        items = hours,
                        selectedItem = selectedHour,
                        onItemSelected = { selectedHour = it }
                    )
                    Text(text = ":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    NumberPicker(
                        items = minutes,
                        selectedItem = selectedMinute,
                        onItemSelected = { selectedMinute = it }
                    )
                    NumberPicker(
                        items = periods,
                        selectedItem = selectedPeriod,
                        onItemSelected = { selectedPeriod = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = {
                        onConfirm("$selectedHour:$selectedMinute $selectedPeriod")
                    }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPicker(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = items.indexOf(selectedItem)
    )

    LaunchedEffect(selectedItem) {
        listState.animateScrollToItem(items.indexOf(selectedItem))
    }

    Box(
        modifier = Modifier
            .width(80.dp)
            .height(120.dp) // Controls the visible number of items
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items) { item ->
                Text(
                    text = item,
                    fontSize = 22.sp,
                    fontWeight = if (item == selectedItem) FontWeight.Bold else FontWeight.Normal,
                    color = if (item == selectedItem) Color.Black else Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemSelected(item) }
                        .padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PeriodDropdown(selectedPeriod: String, onPeriodSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(80.dp)
            .padding(horizontal = 8.dp)
            .clickable { expanded = true }
            .background(Color.LightGray, RoundedCornerShape(4.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = selectedPeriod, fontSize = 16.sp, color = Color.Black)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("AM") },
            onClick = {
                onPeriodSelected("AM")
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("PM") },
            onClick = {
                onPeriodSelected("PM")
                expanded = false
            }
        )
    }
}

fun checkNum(str: String): String {
    return if(str.length == 1) "0$str" else if(str.isEmpty()) "00" else str
}

@Composable
fun SimpleTimeInputField(modifier: Modifier = Modifier, value: String, valueChange: (String) -> Unit, placeholder: String = "") {
    var isFocused by remember { mutableStateOf(false) }
    Box (
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .then(
                if (isFocused) {
                    Modifier.border(1.dp, Color.Blue)
                } else {
                    Modifier.border(1.dp, color = Color.Gray) // Bottom border when not focused
                }
            ),
        contentAlignment = Alignment.CenterStart // Ensures the text is centered
    ) {
        BasicTextField(
            value = value,
            onValueChange = { newText ->
                if (newText.length <= 2) { // Limit input to 2 characters
                    if (validateTimeInput(newText)) {
                        valueChange(newText)
                    }
                }
            },
            modifier = modifier
                .padding(4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number, // Use numeric keyboard
                imeAction = ImeAction.Done
            ),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Show the placeholder if text is empty
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                color = Color.Gray, // Placeholder text color
                                fontSize = 16.sp
                            )
                        )
                    }
                    innerTextField() // The actual text field content
                }
            },
        )
    }
}

@Composable
fun GetHHMM( modifier: Modifier = Modifier, mins: String, setMins: (String) -> Unit, hrs: String, setHrs: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        SimpleTimeInputField(value = hrs, valueChange = setHrs, modifier = Modifier.weight(1f), placeholder = "HH")
        Text(":",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        SimpleTimeInputField(value = mins, valueChange = setMins, modifier = Modifier.weight(1f), placeholder = "MM")
    }

}

fun validateTimeInput(input: String): Boolean {
    if (input.isEmpty()) return true
    val value = input.toIntOrNull() ?: return false
    return value in 0..59 // Validate range (0-59)
}

@Preview(showBackground = true, widthDp = 309, heightDp = 675)
@Composable
fun PreviewTimeSlotPicker() {
//    GetTimeSlotListDialog()
//    TimeSlotSelectionDialog()
//    OpenSimpleTimeSlotListDialog()
    SimpleTimeMaker({}, {})
}