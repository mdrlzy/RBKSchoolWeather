package com.mdrlzy.rbkweather.presentation.navigation

sealed class Destination(val route: String) {
    data object Home : Destination(route = "home")
}
