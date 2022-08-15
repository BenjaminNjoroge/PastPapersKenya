package com.pastpaperskenya.app.business.util

import kotlinx.serialization.json.Json

val appJsonConfig: Json
    get() = Json { ignoreUnknownKeys = true }