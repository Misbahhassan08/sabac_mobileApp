package com.example.carapp.screens.Inspector

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carapp.R
import com.example.carapp.screens.redcolor

@Composable
fun Donecheck(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
    ) {
        HeaderSecti(title = "Time Slot Booked Successfully")
        StepProgressIndicat(currentStep = 2)
        PaymentSect(navController)
    }
}

@Composable
fun HeaderSecti(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Brush.verticalGradient(listOf(redcolor, redcolor)))
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StepProgressIndicat(currentStep: Int) {
    /*  Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
      ) {
          listOf("Basic Info", "Expert Visit", "Checkout").forEachIndexed { index, step ->
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                      Box(
                          contentAlignment = Alignment.Center,
                          modifier = Modifier
                              .size(24.dp)
                              .clip(CircleShape)
                              .background(if (index <= currentStep) Color.Yellow else Color.Gray)
                      ) {
                          if (index < currentStep) {
                              Icon(
                                  imageVector = Icons.Default.Check,
                                  contentDescription = "Checked",
                                  tint = Color.Black,
                                  modifier = Modifier.size(16.dp)
                              )
                          }
                      }
                      if (index < 2) {
                          Spacer(modifier = Modifier.width(8.dp))
                          Box(
                              modifier = Modifier
                                  .width(40.dp)
                                  .height(2.dp)
                                  .background(Color.White)
                          )
                          Spacer(modifier = Modifier.width(8.dp))
                      }
                  }
                  Text(step, color = Color.White, fontSize = 12.sp)
              }
          }
      }*/
}

@Composable
fun PaymentSect(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.verify), // Replace with your verified image resource id
                contentDescription = "Verified",
                modifier = Modifier.size(380.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            PayButt(navController)
        }
    }
}


@Composable
fun PayButt(navController: NavController) {
    Button(
        onClick = {
            navController.navigate("inspector")
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = redcolor)
    ) {
        Text("Done", color = Color.White, fontSize = 16.sp)
    }
}

