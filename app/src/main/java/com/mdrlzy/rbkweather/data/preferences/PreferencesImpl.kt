package com.mdrlzy.rbkweather.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mdrlzy.rbkweather.domain.repository.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val WEATHER_PREFERENCES_NAME = "weather_preferences"
private const val IS_CELCIUS_NOT_FARENHEIT_KEY = "is_celcius_not_farenheit"
private val Context.dataStore by preferencesDataStore(name = WEATHER_PREFERENCES_NAME)

class PreferencesImpl(
    private val context: Context,
) : Preferences {

    private val isCelciusNotFarenheitKey = booleanPreferencesKey(IS_CELCIUS_NOT_FARENHEIT_KEY)

    override val isCelciusNotFarenheit: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isCelciusNotFarenheitKey] ?: true
        }

    override suspend fun setIsCelciusNotFarenheit(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isCelciusNotFarenheitKey] = value
        }
    }
}
