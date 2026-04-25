package com.mdrlzy.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mdrlzy.ui.theme.OnCardContent

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        content()
    }
}

@Composable
fun InfoCardBasic(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    InfoCard(modifier = modifier) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            IconTitle(icon = icon, text = title)
            Spacer(Modifier.height(8.dp))
            CompositionLocalProvider(LocalContentColor provides OnCardContent) {
                content()
            }
        }
    }
}

@Composable
fun InfoCardSmall(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    InfoCardBasic(
        modifier = modifier.height(165.dp),
        icon = icon,
        title = title,
        content = content,
    )
}