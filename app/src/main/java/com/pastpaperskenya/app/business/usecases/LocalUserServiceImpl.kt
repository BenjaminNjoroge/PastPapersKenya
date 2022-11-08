package com.pastpaperskenya.app.business.usecases

import com.pastpaperskenya.app.business.datasources.cache.AppDatabase
import com.pastpaperskenya.app.business.model.auth.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalUserServiceImpl @Inject constructor(
    private val database: AppDatabase
): LocalUserService {

    private val appDao= database.appDao()

    override suspend fun insertUserToDatabase(userDetails: UserDetails): Long {
        return appDao.insertUserDetails(userDetails)
    }

    override fun getUserFromDatabase(userServerId: Int)= appDao.getUserDetails(userServerId)

    override suspend fun updateUserInDatabase(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int) {
        appDao.updateUserDetails(phone, firstname, lastname, country, county, userServerId)
    }

}