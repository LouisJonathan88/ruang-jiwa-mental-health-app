package com.example.ruangjiwa

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SplashScreen() {
    val backgroundColor = Color(0xFFF0F4F7)
    val logoColor = Color(0xFF00CED1)
    val darkTextColor = Color(0xFF2C3E50)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo Ruang Jiwa",
            modifier = Modifier.size(120.dp),
             colorFilter = ColorFilter.tint(logoColor)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Ruang Jiwa",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = darkTextColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Sahabat untuk Kesehatan Mentalmu",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = darkTextColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}