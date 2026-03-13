package com.example.pokemonexplorerapp.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue


@Composable
fun MyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    textColor: Color = Color.Black,
    height: Dp = 80.dp,
    fontSize: TextUnit = 18.sp,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val depth = 6.dp

    val offsetAnimation by animateDpAsState(
        targetValue = if (isPressed && enabled) 0.dp else (-depth),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "buttonPress"
    )

    val shadowColor = if (enabled) Color.Black else Color.LightGray  // ← matches button state

    Box(
        modifier = modifier
            .height(height + depth)
            .padding(top = depth, start = depth)
            .drawBehind {
                drawRoundRect(
                    color = shadowColor,
                    topLeft = Offset.Zero,
                    size = size,
                )
            }
    ) {
        Button(
            onClick = {
                if (!enabled) return@Button
                scope.launch {
                    isPressed = true   // press down
                    delay(100)        // hold
                    isPressed = false  // release
                    delay(50)         // tiny pause so you see the release
                    onClick()          // then navigate
                }
            },
            enabled = enabled,
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxSize()
                .offset(offsetAnimation, offsetAnimation),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                disabledContainerColor = backgroundColor.copy(
                    red = backgroundColor.red * 0.6f + 0.4f,
                    green = backgroundColor.green * 0.6f + 0.4f,
                    blue = backgroundColor.blue * 0.6f + 0.4f,
                )            ),
            border = BorderStroke(2.dp, shadowColor),
            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
        ) {
            Text(
                text = text.uppercase(),
                fontSize = fontSize,
                fontWeight = FontWeight.Black,
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                color = if (enabled) textColor else textColor.copy(alpha = 1f)  // ← faded text
            )
        }
    }
}