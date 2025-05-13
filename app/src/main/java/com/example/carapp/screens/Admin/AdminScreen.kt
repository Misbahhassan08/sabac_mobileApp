package com.example.carapp.screens.Admin

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.carapp.R
import com.example.carapp.models.DealerList
import com.example.carapp.screens.Inspector.CustomAnimatedLoade
import com.example.carapp.screens.Inspector.DrawerIte
import com.example.carapp.screens.Inspector.DrawerIteP
import com.example.carapp.screens.performLogout
import com.example.carapp.screens.redcolor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
/*
fun AdminScreen() {
    val viewModel: AdminViewModel = hiltViewModel()
    val appointments = viewModel.appointments.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Admin",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF282931)
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (appointments.value.isNotEmpty()) {
                appointments.value.forEach { dateWithSlots ->
                    Spacer(Modifier.height(18.dp))
                    val value = dateWithSlots.date?.let { convertMillisToDate(it) }
                    Text(text = "Date: ${value}")
                    Text(text = "Time Slots: ${dateWithSlots.timeSlots}")
                }
                Spacer(Modifier.height(18.dp))
                Button(onClick = {

                }) {
                    Text(text = "Approve", fontWeight = FontWeight.Bold)
                }
            } else {
                Text(text = "No Appointment slots are up for approval")
            }
            Log.d("Appointment SLOTS", appointments.value.toString())
        }
    }
}

*/

fun AdminScreen(
    navController: NavController,
    viewModel: AdminModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    viewModelL: DealerList = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val upcomingCars by viewModel.cars.collectAsState()
    val liveCars by viewModel.assignedSlots.collectAsState()
    val liveCarsL by viewModelL.assignedSlots.collectAsState()




    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false

    var selectedScreen by remember { mutableStateOf("Home") }
    LaunchedEffect(Unit) {
        viewModel.fetchCarList(context)
    }

    var selectedTabIndex by remember { mutableStateOf(0) }
        CustomAnimatedLoade(
            modifier = Modifier.fillMaxSize(),
//            color = Color.Magenta,
//            strokeWidth = 8.dp,
            radius = 40.dp
        )
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(260.dp)
                        .background(Color.Transparent),
                    drawerContainerColor = Color.Transparent
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        redcolor,
                                        redcolor,
                                        redcolor
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(90.dp))

                            Image(
                                painter = painterResource(id = R.drawable.car3),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "SABAC",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Spacer(modifier = Modifier.height(20.dp))

                                    // Navigation Items
                                    DrawerIte(
                                        icon = Icons.Filled.Home,
                                        label = "Home",
                                        onClick = {
                                            selectedScreen = "Home"
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                    DrawerIteP(
                                        icon = painterResource(id = R.drawable.dea),
                                        label = "Manage Dealer",
                                        onClick = {
                                            selectedScreen = "RegisterUserScreen"
                                            scope.launch { drawerState.close() }
                                        }
                                    )

                                    DrawerIteP(
                                        icon = painterResource(id = R.drawable.insp),
                                        label = "Manage Inspector",
                                        onClick = {
                                            selectedScreen = "RegisterInspector"
                                            scope.launch { drawerState.close() }
                                        }
                                    )

                                    DrawerIteP(
                                        icon = painterResource(id = R.drawable.ad),
                                        label = "Manage Admin",
                                        onClick = {
                                            selectedScreen = "RegisterAdmin"
                                            scope.launch { drawerState.close() }
                                        }
                                    )

                                    DrawerIteP(
                                        icon = painterResource(id = R.drawable.bid),
                                        label = "Bidding",
                                        onClick = {
                                            selectedScreen = "ViewBidNotification"
                                            scope.launch { drawerState.close() }
                                        }
                                    )
                                }

                                Column {
                                    Divider(
                                        color = Color.White,
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    DrawerIteP(
                                        icon = painterResource(id = R.drawable.log),
                                        label = "Logout",
                                        onClick = { performLogout(navController, context) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        redcolor,
                                        redcolor,
                                        redcolor,
                                    )
                                )
                            )
                    ) {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = "Admin Dashboard",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    }
                },
            ) { paddingValues ->
                when (selectedScreen) {
                    "Home" -> {
                        var selectedTabIndex by remember { mutableStateOf(0) }
                        val tabTitles = listOf("Upcoming", "Awaiting", "Live")

                        Column(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            androidx.compose.material3.TabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Color(0xFFF84444)
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    androidx.compose.material3.Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = {
                                            Text(
                                                title,
                                                color = if (selectedTabIndex == index) Color.Yellow else Color.White,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        selectedContentColor = Color.White,
                                        unselectedContentColor = Color.Black
                                    )
                                }
                            }

                            when (selectedTabIndex) {
                                0 -> UpcomingList(upcomingCars, navController = navController,)
                                1 -> Awaiting(liveCars, navController = navController)
                                2 -> LiveList( navController = navController)
                            }
                        }
                    }

/*
                    "RegisterUserScreen" -> {
                        var selectedTabIndex by remember { mutableStateOf(0) }
                        val tabTitles = listOf("Dealer List", "Create +")

                        Column(modifier = Modifier.padding(paddingValues)) {
                            androidx.compose.material3.TabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Color(0xFFF84444)
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    androidx.compose.material3.Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = {
                                            Text(
                                                title,
                                                color = if (selectedTabIndex == index) Color.Yellow else Color.White,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        selectedContentColor = Color.White,
                                        unselectedContentColor = Color.Black
                                    )
                                }
                            }

                            when (selectedTabIndex) {
                                0 -> DealerListScreen()
                                1 -> RegisterUserScreen(navController)
                            }
                        }
                    }
*/

                    "RegisterUserScreen" -> {
                        var selectedTabIndex by remember { mutableStateOf(0) }
                        val tabTitles = listOf("Dealer List", "Create +")

                        Column(modifier = Modifier.padding(paddingValues)) {
                            androidx.compose.material3.TabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Color(0xFFF84444)
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    androidx.compose.material3.Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = {
                                            Text(
                                                title,
                                                color = if (selectedTabIndex == index) Color.Yellow else Color.White,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        selectedContentColor = Color.White,
                                        unselectedContentColor = Color.Black
                                    )
                                }
                            }

                            when (selectedTabIndex) {
                                0 -> DealerListScreen()
                                1 -> RegisterUserScreen(navController, onRegistrationSuccess = {
                                    selectedTabIndex = 0 // Switch to Dealer List tab
                                })
                            }
                        }
                    }

                    /*
                                        "RegisterInspector" -> {
                                            var selectedTabIndex by remember { mutableStateOf(0) }
                                            val tabTitles = listOf("Inspector List", "Create +")

                                            Column(modifier = Modifier.padding(paddingValues)) {
                                                androidx.compose.material3.TabRow(
                                                    selectedTabIndex = selectedTabIndex,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    containerColor = Color(0xFFF84444)
                                                ) {
                                                    tabTitles.forEachIndexed { index, title ->
                                                        androidx.compose.material3.Tab(
                                                            selected = selectedTabIndex == index,
                                                            onClick = { selectedTabIndex = index },
                                                            text = {
                                                                Text(
                                                                    title,
                                                                    color = if (selectedTabIndex == index) Color.Yellow else Color.White,
                                                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                                                )
                                                            },
                                                            selectedContentColor = Color.White,
                                                            unselectedContentColor = Color.Black
                                                        )
                                                    }
                                                }

                                                when (selectedTabIndex) {
                                                    0 -> InspectorListScreen()
                                                    1 -> RegisterInspectorScreen(navController)
                                                }
                                            }
                                        }
                    */


                    "RegisterInspector" -> {
                        val tabTitles = listOf("Inspector List", "Create +")

                        Column(modifier = Modifier.padding(paddingValues)) {
                            androidx.compose.material3.TabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Color(0xFFF84444)
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    androidx.compose.material3.Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = {
                                            Text(
                                                title,
                                                color = if (selectedTabIndex == index) Color.Yellow else Color.White,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        selectedContentColor = Color.White,
                                        unselectedContentColor = Color.Black
                                    )
                                }
                            }
                            when (selectedTabIndex) {
                                0 -> InspectorListScreen()
                                1 -> RegisterInspectorScreen(navController, onRegistrationSuccess = {
                                    // Navigate to the Inspector List tab (index 0)
                                    selectedTabIndex = 0
                                })
                            }
                        }
                    }

/*
                    "RegisterAdmin" -> {
                        var selectedTabIndex by remember { mutableStateOf(0) }
                        val tabTitles = listOf("Admin List", "Create +")

                        Column(modifier = Modifier.padding(paddingValues)) {
                            androidx.compose.material3.TabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Color(0xFFF84444)
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    androidx.compose.material3.Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = {
                                            Text(
                                                title,
                                                color = if (selectedTabIndex == index) Color.Yellow else Color.White,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        selectedContentColor = Color.White,
                                        unselectedContentColor = Color.Black
                                    )
                                }
                            }

                            when (selectedTabIndex) {
                                0 -> AdminListScreen()
                                1 -> RegisterAdminScreen(navController)
                            }
                        }
                    }
*/
                    "RegisterAdmin" -> {
                        var selectedTabIndex by remember { mutableStateOf(0) }
                        val tabTitles = listOf("Admin List", "Create +")

                        Column(modifier = Modifier.padding(paddingValues)) {
                            androidx.compose.material3.TabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Color(0xFFF84444)
                            ) {
                                tabTitles.forEachIndexed { index, title ->
                                    androidx.compose.material3.Tab(
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index },
                                        text = {
                                            Text(
                                                title,
                                                color = if (selectedTabIndex == index) Color.Yellow else Color.White,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        selectedContentColor = Color.White,
                                        unselectedContentColor = Color.Black
                                    )
                                }
                            }

                            when (selectedTabIndex) {
                                0 -> AdminListScreen()
                                1 -> RegisterAdminScreen(navController, onRegistrationSuccess = {
                                    selectedTabIndex = 0
                                })
                            }
                        }
                    }


                    "ViewBidNotification" -> {
                        BiddingScreen(navController = navController)
                    }
                }
            }
        }
    }


