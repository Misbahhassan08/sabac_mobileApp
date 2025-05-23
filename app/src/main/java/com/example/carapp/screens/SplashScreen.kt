package com.example.carapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carapp.R
import com.example.carapp.assets.redcolor
import com.example.carapp.ui.theme.lexendFont


@Composable
fun SplashScreen(onClick: () -> Unit) {
    val bgColor = redcolor
    val txtColor = Color(0xFFFFFFFF)
    Box(
        modifier = Modifier.background(bgColor).padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.car1),
                contentDescription = "Cars",
                modifier = Modifier.size(150.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.car2),
                contentDescription = "car",
                modifier = Modifier.size(280.dp)
            )
            Text(text = "Premium Cars.", color = txtColor, fontSize = 28.sp, fontWeight = FontWeight.Bold,fontFamily = lexendFont, )
            Text(text = "Enjoy the luxury", color = txtColor, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = lexendFont,)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Premium and prestige car daily rental", color = txtColor, fontSize = 14.sp,fontFamily = lexendFont,)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = "Experience the thrill at a lower price", color = txtColor, fontSize = 14.sp,fontFamily = lexendFont,)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onClick() },
                elevation = ButtonDefaults.buttonElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Text(text = "Let's Go", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp,fontFamily = lexendFont,)
            }
        }

    }
}
@Preview(showBackground = true, widthDp = 309, heightDp = 675)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(onClick = {})
}