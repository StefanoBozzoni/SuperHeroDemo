package com.example.superheroesdemo.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun FavoriteHeart(modifier: Modifier = Modifier, offsetX: Dp, offsetY: Dp, isLiked: Boolean?) {
    isLiked?.let {
        Box(
            modifier = Modifier
                .then(modifier)
                .offset(x = offsetX, y = offsetY)
                .zIndex(10f)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.HeartBroken,
                contentDescription = "",
                tint = Color.Red
            )
        }
    }
}