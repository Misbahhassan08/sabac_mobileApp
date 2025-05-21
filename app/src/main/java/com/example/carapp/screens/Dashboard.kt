package com.example.carapp.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.carapp.R
import com.example.carapp.assets.redcolor
import com.example.carapp.assets.seller_Color
import com.example.carapp.models.CarListViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    navController: NavController,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    val systemUiController = rememberSystemUiController()
    var showExitDialog by remember { mutableStateOf(false) }
    val images = listOf(
        R.drawable.s1,
        R.drawable.s2,
        R.drawable.s3
    )

//    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { images.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Delay 3 seconds
            val nextPage = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    // Back press handler
    BackHandler {
        showExitDialog = true
    }

    //

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "Exit App?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to exit the application?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        (context as? Activity)?.finish()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Yes, Exit")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showExitDialog = false },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }


    systemUiController.isStatusBarVisible = false

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
                            redcolor
//                                brush = Brush.verticalGradient(
//                                    colors = listOf(
//                                        Color(0xFF0D47A1),
//                                        Color(0xFF0D47A1),
//                                        Color(0xFF42A5F5)
//                                    )
//                                )
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(90.dp))

                        // Profile Image
                        Image(
                            painter = painterResource(id = R.drawable.car3),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
//                                    .border(2.dp, Color.White, CircleShape)
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

                                DrawerItem(
                                    icon = Icons.Filled.Home,
                                    label = "Home",
                                    onClick = {})
                                Divider(
                                    color = Color.White,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                DrawerItem(
                                    icon = Icons.Filled.Call,
                                    label = "Contact Us",
                                    onClick = {})
                                Divider(
                                    color = Color.White,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                DrawerItem(
                                    icon = Icons.Filled.LocationOn,
                                    label = "Our Location",
                                    onClick = {})
                                Divider(
                                    color = Color.White,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                DrawerItem(
                                    icon = Icons.Filled.AccountCircle,
                                    label = "Login",
                                    onClick = {
                                        scope.launch {
                                            drawerState.close()
                                            navController.navigate("login")
                                        }
                                    }
                                )
                                Divider(
                                    color = Color.White,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            /*
                                Column {
                                    Divider(
                                        color = Color.White,
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )

                                    DrawerItem(
                                        icon = Icons.Filled.ExitToApp,
                                        label = "Logout",
                                        onClick = { performLogout(navController, context) }
                                    )
                                }
*/
                        }
                    }
                }
            }
        }
  /*  ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Reports History",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },

                    )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
//                            .padding(8.dp, 16.dp)
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
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
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
                        text = "Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.top_image),
                        contentDescription = "Fixed Top Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
//                            .height(230.dp)
                    )

//                    Spacer(modifier = Modifier.height(1.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                            modifier = Modifier
                                .fillMaxWidth()
//                                .width(260.dp)
                                .height(220.dp)
                        ) { page ->
                            val pageOffset = (
                                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                    ).absoluteValue

                            val scale = if (page == pagerState.currentPage) 1f else 0.85f
                            var alpha = if (page == pagerState.currentPage) 1f else 0.7f

                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        alpha = alpha
                                    }
                                    .fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = images[page]),
                                    contentDescription = "Slider Image",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Car Fast",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    Text(
                        text = "Sell Your Car Faster.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            navController.navigate("postcar")
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = redcolor),
                    ) {
                        Text(
                            "Post Ad +",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}*/
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Reports History",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    modifier = Modifier.shadow(elevation = 4.dp)
                )
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
                                )
                            )
                        )
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = "Dashboard",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White,
//                        modifier = Modifier.shadow(
//                            color = Color.Black.copy(alpha = 0.2f),
//                            blurRadius = 2.dp
//                        )
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFAFAFA))  // Light background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),  // Make it scrollable
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Fixed Top Image
                    Image(
                        painter = painterResource(id = R.drawable.top_image),
                        contentDescription = "Fixed Top Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    // Slider Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                            modifier = Modifier
                                .fillMaxWidth(),
                            pageSpacing = (-35).dp,
                            contentPadding = PaddingValues(horizontal = 60.dp),
                            pageSize = PageSize.Fixed(300.dp)
                        ) { page ->
                            val pageOffset = (
                                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                    ).absoluteValue

                            val scale = lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            var alpha = lerp(
                                start = 0.7f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )

                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        alpha = alpha
                                    }
//                                    .fillMaxSize()
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = images[page]),
                                    contentDescription = "Slider Image",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    // Pager indicators (minimal style)
                    Row(
                        Modifier
                            .height(16.dp)
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color = if (pagerState.currentPage == iteration) redcolor else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(8.dp) // Smaller indicators
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Title and Description
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "Car Fast",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Sell Your Car Faster With Our Premium Services.",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button
                    Button(
                        onClick = { navController.navigate("postcar") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = redcolor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 48.dp)
                            .height(56.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Post Ad",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}