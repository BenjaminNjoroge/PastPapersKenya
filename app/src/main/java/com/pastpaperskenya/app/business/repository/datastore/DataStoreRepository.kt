package com.pastpaperskenya.app.business.repository.datastore

import androidx.datastore.preferences.core.Preferences

interface DataStoreRepository {
    suspend fun setValue(key:String, value: String)
    suspend fun getValue(key: String): String?
    suspend fun <T> remove(key: Preferences.Key<T>)
    suspend fun clear()
}