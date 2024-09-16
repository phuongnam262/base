package com.lock.smartlocker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lock.smartlocker.data.models.UserLockerModel

@Dao
interface UserLockerDAO{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserLockerModel) : Long

    @Update
    suspend fun updateUser(user: UserLockerModel) : Int

    @Delete
    suspend fun deleteUser(user: UserLockerModel)

    @Query("DELETE FROM UserLockerModel WHERE personCode = :personCode")
    fun deleteUserByCode(personCode: String) : Int

    @Query("DELETE FROM UserLockerModel")
    fun deleteAllUser() : Int

    @Query("SELECT * FROM UserLockerModel WHERE personCode = :personCode")
    suspend fun getUserLocker(personCode: String) : UserLockerModel?

    @Query("SELECT * FROM UserLockerModel")
    suspend fun getAllUserLocker() : List<UserLockerModel>

    @Query("SELECT * FROM UserLockerModel WHERE email = :email")
    suspend fun checkUserEmail(email: String) : UserLockerModel?

    @Query("SELECT * FROM UserLockerModel WHERE cardNumber = :cardNumber")
    suspend fun checkUserCard(cardNumber: String) : UserLockerModel?
}