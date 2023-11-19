package com.pastpaperskenya.papers.business.usecases

import com.pastpaperskenya.papers.business.datasources.cache.AppDatabase
import com.pastpaperskenya.papers.business.model.user.UserDetails
import javax.inject.Inject

class LocalUserServiceImpl @Inject constructor(
    private val database: AppDatabase
): LocalUserService {

    private val appDao= database.appDao()

    override suspend fun insertUserToDatabase(userDetails: UserDetails): Long {
        return appDao.insertUserDetails(userDetails)
    }

    override fun getUserFromDatabase(userServerId: Int)= appDao.getUserDetails(userServerId)

    override suspend fun updateUserInDatabase(phone: String, firstname: String, lastname: String, country: String, county: String, userServerId: Int, photo: String?) {
        appDao.updateUserDetails(phone, firstname, lastname, country, county, userServerId, photo)
    }

    override suspend fun deleteUserInLocalDatabase(userServerId: Int) = appDao.deleteUserInLocalDatabase(userServerId)


}