package com.mdrlzy.rbkweather.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mdrlzy.ui.theme.BlueGradient
import com.mdrlzy.ui.theme.BluePrimary
import com.mdrlzy.ui.theme.BluePrimaryContainer
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.OutlineLight

@Composable
fun HomeBottomNavBar(
    modifier: Modifier = Modifier,
    onMapClick: () -> Unit = {},
    onListClick: () -> Unit = {},
    currentPage: Int = 0,
    pageCount: Int = 0,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 28.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HomeIconButton(onClick = onMapClick) {
            Icon(
                painter = painterResource(CoreRDrawable.map),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified,
            )
        }

        HomeCenterPanel {
            PagerStatusIndicator(
                currentPage = currentPage,
                pageCount = pageCount,
            )
        }

        HomeIconButton(onClick = onListClick) {
            Icon(
                painter = painterResource(CoreRDrawable.list),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified,
            )
        }
    }
}

@Composable
private fun HomeIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .shadow(
                elevation = 4.dp,
                shape = CircleShape,
                spotColor = Color(0x29000000),
                ambientColor = Color(0x29000000)
            )
            .clip(CircleShape)
            .background(BlueGradient)
            .border(1.dp, OutlineLight, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun HomeCenterPanel(
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(40.dp),
                spotColor = Color(0x29000000),
                ambientColor = Color(0x29000000)
            )
            .clip(RoundedCornerShape(40.dp))
            .background(BlueGradient)
            .border(1.dp, OutlineLight, RoundedCornerShape(40.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        content = content,
    )
}

@Composable
private fun PagerStatusIndicator(
    currentPage: Int,
    pageCount: Int,
) {
    val safePageCount = pageCount.coerceAtLeast(1)

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(safePageCount) { index ->
            if (index == currentPage.coerceIn(0, safePageCount - 1)) {
                Icon(
                    painter = painterResource(CoreRDrawable.cursor),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified,
                )
            } else {
                Box(
                    Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.52f)),
                )
            }
        }
    }
}
