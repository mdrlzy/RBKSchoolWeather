@file:OptIn(ExperimentalMaterial3Api::class)

package com.mdrlzy.rbkweather.presentation.citylist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mdrlzy.ui.theme.AppBackground
import com.mdrlzy.ui.theme.BlueGradient
import com.mdrlzy.ui.theme.CoreRDrawable
import com.mdrlzy.ui.theme.CoreRString
import com.mdrlzy.ui.theme.OnWeatherDescription
import com.mdrlzy.ui.theme.OutlineLight
import com.mdrlzy.ui.theme.RBKWeatherTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CityListScreen(
    viewModel: CityListViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isMenuBottomSheetVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CityListEffect.ShowMenuBottomSheet -> {
                    isMenuBottomSheetVisible = true
                }
                CityListEffect.HideMenuBottomSheet -> {
                    isMenuBottomSheetVisible = false
                }
            }
        }
    }

    CityListScreenContent(
        state = state,
        modifier = modifier,
        onMoreClick = viewModel::onMoreClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
    )

    if (isMenuBottomSheetVisible) {
        CityListMenuBottomSheet(
            onTemperatureUnitClick = viewModel::onTemperatureUnitClick,
            onDismissRequest = viewModel::onMenuDismiss
        )
    }
}

@Composable
private fun CityListScreenContent(
    state: CityListScreenState,
    modifier: Modifier = Modifier,
    onMoreClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    val navigationBarBottomPadding = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Spacer(Modifier.height(12.dp))
            CityListHeader(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = stringResource(CoreRString.city_list_title),
                onClick = onMoreClick,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 12.dp,
                    end = 16.dp,
                    bottom = 112.dp + navigationBarBottomPadding,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = state.filteredCities,
                    key = { it.id },
                ) { city ->
                    CityWeatherCard(
                        item = city,
                        isCelciusNotFarenheit = state.isCelciusNotFarenheit,
                    )
                }
                item {
                    Spacer(Modifier.height(8.dp))
                    CityListFooter()
                }
            }
        }

        CityListSearchBar(
            value = state.searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = stringResource(CoreRString.city_list_search_placeholder),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 28.dp, vertical = 28.dp),
        )
    }
}

@Composable
private fun CityListHeader(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge
        )
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
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(CoreRDrawable.more__dots),
                contentDescription = null,
                tint = Color.White,
            )
        }
    }
}

@Composable
private fun CityWeatherCard(
    item: CityWeatherCardUiItem,
    isCelciusNotFarenheit: Boolean,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(22.dp)
    val noValue = stringResource(CoreRString.no_value)
    val cityName = item.cityName ?: stringResource(CoreRString.unknown_city)
    val subtitle = item.subtitle ?: stringResource(CoreRString.no_data)
    val condition = item.condition ?: stringResource(CoreRString.weather_no_data)
    val temperatureUnit = stringResource(
        if (isCelciusNotFarenheit) {
            CoreRString.degrees_celsius_symbol
        } else {
            CoreRString.degrees_fahrenheit_symbol
        }
    )
    val temperature = if (item.temperature != null) {
        stringResource(CoreRString.temperature_with_unit, item.temperature, temperatureUnit)
    } else {
        noValue
    }
    val minTemperature = if (item.minTemperature != null) {
        stringResource(CoreRString.temperature_with_unit, item.minTemperature, temperatureUnit)
    } else {
        noValue
    }
    val maxTemperature = if (item.maxTemperature != null) {
        stringResource(CoreRString.temperature_with_unit, item.maxTemperature, temperatureUnit)
    } else {
        noValue
    }
    val temperatureRange = stringResource(CoreRString.max_min_temp, maxTemperature, minTemperature)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(shape),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(CoreRDrawable.bg_clear_day),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cityName,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = OnWeatherDescription,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Text(
                    text = temperature,
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.W300,
                    letterSpacing = 4.sp,
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = condition,
                    color = OnWeatherDescription,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = temperatureRange,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Composable
private fun CityListFooter(
    modifier: Modifier = Modifier,
) {
    val prefix = stringResource(CoreRString.city_list_footer_prefix)
    val firstLink = stringResource(CoreRString.city_list_footer_link_meteo)
    val between = stringResource(CoreRString.city_list_footer_between)
    val secondLink = stringResource(CoreRString.city_list_footer_link_carto)
    val suffix = stringResource(CoreRString.city_list_footer_suffix)

    val annotated = buildAnnotatedString {
        append(prefix)
        withStyle(
            SpanStyle(
                color = OutlineLight,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append(firstLink)
        }
        append(between)
        withStyle(
            SpanStyle(
                color = OutlineLight,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append(secondLink)
        }
        append(suffix)
    }
    Text(
        text = annotated,
        modifier = modifier.fillMaxWidth(),
        color = OutlineLight,
        fontSize = 10.sp,
        fontWeight = FontWeight.W500,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun CityListSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .size(44.dp)
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
    ) {
        Icon(
            painter = painterResource(CoreRDrawable.search),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.size(12.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = OutlineLight,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                innerTextField()
            },
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = painterResource(CoreRDrawable.microphone),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2B4F73)
@Composable
private fun CityListScreenPreview() {
    RBKWeatherTheme {
        val cities = listOf(
            CityWeatherCardUiItem(
                id = 1L,
                cityName = "Astana",
                subtitle = "18:09",
                condition = "Clear",
                temperature = 18,
                minTemperature = 12,
                maxTemperature = 21,
            ),
            CityWeatherCardUiItem(
                id = 2L,
                cityName = "Almaty",
                subtitle = "18:09",
                condition = "Cloudy",
                temperature = 15,
                minTemperature = 9,
                maxTemperature = 17,
            ),
        )

        CityListScreenContent(
            state = CityListScreenState(
                allCities = cities,
                filteredCities = cities,
            ),
            onMoreClick = {},
            onSearchQueryChange = {},
        )
    }
}

