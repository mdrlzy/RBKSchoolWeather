@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.rbkweather.presentation.citylist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import com.mdrlzy.ui.components.AppHorDivider
import com.mdrlzy.ui.theme.BlueBottomSheet
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.CoreRString

@Composable
fun CityListMenuBottomSheet(
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = BlueBottomSheet,
        contentColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(40.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.White.copy(alpha = 0.7f))
            )
        },
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.height(24.dp))
            CityListMenuItem(
                iconRes = CoreRDrawable.pencil,
                title = stringResource(CoreRString.edit_list),
                onClick = {},
            )
            Spacer(Modifier.height(20.dp))
            CityListMenuItem(
                iconRes = CoreRDrawable.bell,
                title = stringResource(CoreRString.notifications),
                onClick = {},
            )
            Spacer(Modifier.height(16.dp))
            AppHorDivider(Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(16.dp))
            CityListMenuItem(
                iconRes = CoreRDrawable.celsius,
                title = stringResource(CoreRString.celsius),
                onClick = {},
            )
            Spacer(Modifier.height(20.dp))
            CityListMenuItem(
                iconRes = CoreRDrawable.fahrenheit,
                title = stringResource(CoreRString.fahrenheit),
                onClick = {},
            )
            Spacer(Modifier.height(16.dp))
            AppHorDivider(Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(16.dp))
            CityListMenuItem(
                iconRes = CoreRDrawable.colums,
                title = stringResource(CoreRString.units),
                onClick = {},
            )
            Spacer(Modifier.height(16.dp))
            AppHorDivider(Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(16.dp))
            CityListMenuItem(
                iconRes = CoreRDrawable.message,
                title = stringResource(CoreRString.report_problem),
                onClick = {},
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CityListMenuItem(
    iconRes: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp),
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
