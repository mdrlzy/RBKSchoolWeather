package com.mdrlzy.rbkweather.presentation.mapper

import kotlin.math.roundToInt

fun Double.celsiusToFahrenheit(): Double {
    return this * 9 / 5 + 32
}

fun Double.toDisplayTemperature(isCelciusNotFarenheit: Boolean): Int {
    val temperature = if (isCelciusNotFarenheit) {
        this
    } else {
        celsiusToFahrenheit()
    }

    return temperature.roundToInt()
}
