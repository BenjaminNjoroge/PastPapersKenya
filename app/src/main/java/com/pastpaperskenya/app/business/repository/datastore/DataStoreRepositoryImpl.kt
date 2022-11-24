package com.pastpaperskenya.app.business.repository.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


class DataStoreRepositoryImpl( val context: Context) :DataStoreRepository{

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app_prefs")

    override suspend fun setValue(key: String, value: String) {
        context.dataStore.edit{
            it[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getValue(key: String): String? {
        return context.dataStore.data.first()[stringPreferencesKey(key)]
    }

    override suspend fun <T> clear(key: Preferences.Key<T>) {
        context.dataStore.edit { preferences->
            preferences.remove(key)
        }
    }
}

