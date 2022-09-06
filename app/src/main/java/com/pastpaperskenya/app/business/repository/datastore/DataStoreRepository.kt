package com.pastpaperskenya.app.business.repository.datastore

interface DataStoreRepository {
    suspend fun setValue(key:String, value: String)
    suspend fun getValue(key: String): String?
}