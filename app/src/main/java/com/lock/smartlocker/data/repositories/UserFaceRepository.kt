package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.db.AppDatabase
import com.lock.smartlocker.data.entities.AddGroupModel
import com.lock.smartlocker.data.entities.UserLockerModel
import com.lock.smartlocker.data.entities.request.AddPersonRequest
import com.lock.smartlocker.data.entities.request.ImageBase64Request
import com.lock.smartlocker.data.entities.request.ImageSearchRequest
import com.lock.smartlocker.data.entities.responses.BaseFaceResponse
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.DetectImageResponse
import com.lock.smartlocker.data.entities.responses.SearchResponse
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.SafeFaceApiRequest

class UserFaceRepository(
    private val api: LockerAPI,
    private val db: AppDatabase
) : SafeFaceApiRequest() {

    /*
    * call API check Status
    * */
    suspend fun getStatusAPI(): BaseFaceResponse {
        return apiRequest { api.provideFaceAPIService().getStatus() }
    }

    /*
    * call API add group
    * */
    suspend fun addGroup(addGroupModel: AddGroupModel): BaseFaceResponse {
        return apiRequest { api.provideFaceAPIService().addGroup(addGroupModel) }
    }

    /*
    * call API detect Image
    * */
    suspend fun detectImage(strBase64: ImageBase64Request): DetectImageResponse {
        return apiRequest { api.provideFaceAPIService().detectImage(strBase64) }
    }

    /*
    * call API search person
    * */
    suspend fun searchPerson(strBase64: ImageSearchRequest): SearchResponse {
        return apiRequest { api.provideFaceAPIService().searchPerson(strBase64) }
    }

    /*
    * call API add person
    * */
    suspend fun addPerson(addPersonRequest: AddPersonRequest): BaseFaceResponse {
        return apiRequest { api.provideFaceAPIService().addPerson(addPersonRequest) }
    }

    /*
    * call API delete person
    * */
    suspend fun deletePerson(personCode: String): BaseFaceResponse {
        return apiRequest { api.provideFaceAPIService().deletePerson(personCode) }
    }

    /*
    * save user to local database
    * */
    suspend fun saveUser(user: UserLockerModel) = db.getUserLockerDAO().insertUser(user)

    /*
    * get info user registed locker on DB
    * */
    suspend fun getUsedLocker(personCode: String) = db.getUserLockerDAO().getUserLocker(personCode)

    /*
    * delete user registed locker on DB
    * */
    suspend fun deleteUsedLocker(personCode: String) = db.getUserLockerDAO().deleteUserByCode(personCode)
}