package com.example.pokemonexplorerapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyCard(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    shadowOffset: Int = 4,
    borderWidth: Int = 3,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .offset(shadowOffset.dp, shadowOffset.dp) // The hard "shadow"
            .background(Color.Black)
            .then(
                Modifier
                    .offset((-shadowOffset).dp, (-shadowOffset).dp) // Move card up
                    .background(backgroundColor)
                    .border(borderWidth.dp, Color.Black)
            )
    ) {
        content()
    }
}