package com.example.carapp

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.carapp.screens.Admin.AdminReport
import com.example.carapp.screens.Admin.AdminScreen
import com.example.carapp.screens.Admin.InspectorListScreen
import com.example.carapp.screens.BasicInfoScreen
import com.example.carapp.screens.BasicScreen
import com.example.carapp.screens.BookExpertVisit
import com.example.carapp.screens.CalenderDate
import com.example.carapp.screens.CarListScreen
import com.example.carapp.screens.CarSellScreen
import com.example.carapp.screens.Checkout
import com.example.carapp.screens.Dashboard
import com.example.carapp.screens.Dealer.DealerListScreen
//import com.example.carapp.screens.Dealer.ViewDealerReport
import com.example.carapp.screens.Dealer.ViewDealerReports
import com.example.carapp.screens.Expert
import com.example.carapp.screens.Guest.CallExpert
import com.example.carapp.screens.Guest.GuestScreen
import com.example.carapp.screens.Guest.PostDetail
import com.example.carapp.screens.Inspector.AddTimeSlotScreen
import com.example.carapp.screens.Inspector.AssignSlotScreen
import com.example.carapp.screens.Inspector.CarInspectionUI
import com.example.carapp.screens.Inspector.Checkou
import com.example.carapp.screens.Inspector.Donecheck
import com.example.carapp.screens.Inspector.Inspectionreport
import com.example.carapp.screens.Inspector.InspectorListScree
import com.example.carapp.screens.Inspector.PostTestCase
import com.example.carapp.screens.Inspector.TestCase
//import com.example.carapp.screens.Inspector.CarInspectionUI
import com.example.carapp.screens.Inspector.ViewReportScree
import com.example.carapp.screens.Inspector.ViewReportScreen
//import com.example.carapp.screens.InspectorListScree
import com.example.carapp.screens.LoginScreen
import com.example.carapp.screens.RegisterScreen
import com.example.carapp.screens.SplashScreen
//import com.example.carapp.screens.notification.requestNotificationPermission
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
//            requestNotificationPermission()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable(route = "splash") {
            SplashScreen{
                navController.navigate("dash")
            }
        }

        composable(route = "login") {
            LoginScreen(navController)
        }
        composable(route = "dash") {
            Dashboard(navController)
        }
        composable(route = "Test") {
//            TestCase(navController)
            CarInspectionUI()
//            InspectionReportScreen()
        }
        composable(route = "Test1") {
            TestCase(navController)

        }
        composable("PostTest") {
            PostTestCase(navController)
        }

        composable(route = "inspector") {
            InspectorListScree(navController)
        }

        composable(route = "registration") {
            RegisterScreen(navController)
        }
        composable(route = "seller") {
            CarListScreen(navController)
        }
        composable(route = "postcar") {
            CarSellScreen(navController)
        }
        composable("date") {
            CalenderDate()
        }
        composable("inspector_list_screen") {
            InspectorListScreen()
        }
//        composable("date&time") {
//            BasicScreen(navController)
//        }
        composable("datetime/{scheduleData}") { backStackEntry ->
            val scheduleData = backStackEntry.arguments?.getString("scheduleData") ?: "No Data"
            BasicScreen(navController,scheduleData)
        }
//        composable("inspection") {
//            InspectorScreen(navController)
//        }
        composable("admin") {
            AdminScreen(navController)
        }
        composable("info") {
            BasicInfoScreen(navController)
        }
        composable("check") {
            Checkout(navController)
        }
        composable("done") {
            Checkou(navController)
        }
//        composable("report") {
//            Inspectionreport(navController)
//        }
        composable("report/{carId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("carId") ?: ""
            Inspectionreport(navController, id)
        }

        composable("bookexpertvisit/{data}",
            arguments = listOf(navArgument("data") { type = NavType.StringType })
        ) { backStackEntry ->
            val jsonData = backStackEntry.arguments?.getString("data") ?: "[]"
            val list: List<Expert> = Gson().fromJson(jsonData, object : TypeToken<List<Expert>>() {}.type)
            BookExpertVisit(navController, list)
        }

        composable("viewReport/{salerCarId}") { backStackEntry ->
            val sellerCarId = backStackEntry.arguments?.getString("salerCarId") ?: ""
            Log.d("ViewReportScreen", "Navigating with salerCarId: $sellerCarId")
            ViewReportScreen(sellerCarId, navController)
        }
        composable("viewReports/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            ViewReportScree(carId, navController)
        }

        composable("viewReportAdmin/{carId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            AdminReport(carId, navController)
        }

        composable("dealer") {
            DealerListScreen(navController)
        }
        composable("viewDealer/{salerCarId}") { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("salerCarId") ?: ""
            ViewDealerReports(carId, navController)
        }

        composable("post") {
            GuestScreen(navController)
        }
        composable("time") {
            AddTimeSlotScreen(navController)
        }
        composable("guest/{guestId}") { backStackEntry ->
            val guestId = backStackEntry.arguments?.getString("guestId") ?: "Unknown"
            PostDetail(navController, guestId)
        }

        /*composable("CallExpert/{data}",
            arguments = listOf(navArgument("data") { type = NavType.StringType })
        ) { backStackEntry ->
            val jsonData = backStackEntry.arguments?.getString("data") ?: "[]"
            val list: List<Expert> = Gson().fromJson(jsonData, object : TypeToken<List<Expert>>() {}.type)
            CallExpert(navController, list)
        }*/
        composable(
            "CallExpert/{car_id}/{data}",
            arguments = listOf(
                navArgument("car_id") { type = NavType.IntType },
                navArgument("data") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getInt("car_id") ?: 0
            val jsonData = backStackEntry.arguments?.getString("data") ?: "[]"
            val list: List<Expert> = Gson().fromJson(jsonData, object : TypeToken<List<Expert>>() {}.type)

            CallExpert(navController, carId, list)
        }
        composable(
            "assignSlot/{carId}?scheduleData={scheduleData}",
            arguments = listOf(navArgument("scheduleData") { nullable = true })
        ) { backStackEntry ->
            val scheduleData = backStackEntry.arguments?.getString("scheduleData")?.let {
                Uri.decode(it)
            } ?: "No Data"

            val carId = backStackEntry.arguments?.getString("carId")

            AssignSlotScreen(navController, scheduleData, carId)
        }

        composable("DoneCheck") {
            Donecheck(navController)
        }
    }
}
