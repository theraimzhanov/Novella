package com.example.novella.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.novella.components.NovellaLogo
import com.example.novella.navigation.NovellaScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NovellaSplashScreen(navController: NavController) {

    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 0.75f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = {
                    OvershootInterpolator(8f).getInterpolation(it)
                }
            )
        )

        val destination =
            if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
                NovellaScreens.LoginScreen.name
            else
                NovellaScreens.HomeScreen.name

        navController.navigate(destination) {
            popUpTo(NovellaScreens.SplashScreen.name) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NovellaLogo()
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "\"Read. Change. Yourself\"",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.LightGray
            )
        }
    }
}
