package com.lock.smartlocker.ui.retrieve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.request.RetrieveItemRequest
import com.lock.smartlocker.data.models.Categories
import com.lock.smartlocker.data.models.LockerRetrieve
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class RetrieveViewModel(
    private val managerRepository: ManagerRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {

    var retrieveListener: RetrieveListener? = null

    private val _categoriesRetrieve = MutableLiveData<List<Categories>>()
    val categoriesRetrieve: LiveData<List<Categories>> get() = _categoriesRetrieve

    private val _retrieveModels = MutableLiveData<List<LockerRetrieve>>()
    val retrieveModels: LiveData<List<LockerRetrieve>> get() = _retrieveModels
    private var listLockerId = MutableLiveData<List<String>>()
    var categoryIdSelected = MutableLiveData<String>()
    var listSerialRetrieve = MutableLiveData<List<String>>()

    fun getAllItemRetrieve() {
        ioScope.launch {
            mLoading.postValue(true)
            managerRepository.getAllItemRetrieve().apply {
                if (isSuccessful) {
                    if (data != null) {
                        val listId = ArrayList<String>()
                        _categoriesRetrieve.postValue(data.categories)
                        data.categories.map { categories ->
                            listId.addAll(categories.modelRetrievies.flatMap { model ->
                                model.lockers.map { it.lockerId }
                            })
                            categories.modelRetrievies.flatMap { model ->
                                model.lockers.map { it.doorStatus = 2 } }
                        }
                        listLockerId.postValue(listId)
                    }
                } else {
                    handleError(status)
                    _categoriesRetrieve.postValue(emptyList())
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun onCategorySelected(category: Categories) {
        _categoriesRetrieve.value?.map { it.isSelected = false }
        categoryIdSelected.postValue(category.categoryId)
        category.isSelected = true
        val categoriesItem =
            _categoriesRetrieve.value?.find { it.categoryId == category.categoryId }
        categoriesItem.let { categories ->
            val listLocker = categories?.modelRetrievies?.flatMap { it.lockers }?.sortedBy { it.lockerName }
            _retrieveModels.postValue(listLocker ?: emptyList())
        }
    }

    fun getItemFaulty() {
        ioScope.launch {
            mLoading.postValue(true)
            managerRepository.getItemFaulty().apply {
                if (isSuccessful) {
                    if (data != null) {
                        val listId = ArrayList<String>()
                        val listModel = ArrayList<LockerRetrieve>()
                        _categoriesRetrieve.postValue(data.categories)
                        data.categories.map { categories ->
                            listId.addAll(categories.modelRetrievies.flatMap { model ->
                                model.lockers.map { it.lockerId }
                            })
                            listModel.addAll(categories.modelRetrievies.flatMap { it.lockers }.sortedBy { it.lockerName })
                        }
                        listLockerId.postValue(listId)
                        _retrieveModels.postValue(listModel)
                    }
                }else {
                    handleError(status)
                    _retrieveModels.postValue(emptyList())
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun openAllLocker() {
        ioScope.launch {
            val request = HardwareControllerRequest(
                lockerIds = listLockerId.value,
                userHandler = PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
                openType = 2
            )
            mLoading.postValue(true)
            hardwareControllerRepository.openMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        val serialRetrieve = ArrayList<String>()
                        val listDoorStatus = ArrayList<Int>()
                        _categoriesRetrieve.value?.map { categories ->
                            categories.modelRetrievies.map { model ->
                                model.lockers.map { locker ->
                                    val matchingStatus =
                                        data.locker_list.find { it.lockerId == locker.lockerId }
                                    if (matchingStatus != null) {
                                        locker.doorStatus = matchingStatus.doorStatus
                                        if (matchingStatus.doorStatus == 0){
                                            serialRetrieve.add(locker.serialNumber)
                                        }
                                        listDoorStatus.add(locker.doorStatus)
                                    }
                                }
                            }
                        }
                        checkStatusDoor(listDoorStatus)
                        if (serialRetrieve.isNotEmpty()){
                            listSerialRetrieve.postValue(serialRetrieve)
                        }
                        uiScope.launch { retrieveListener?.openLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun checkStatusDoor(doorStatus: List<Int>?) {
        val allNotOpen = doorStatus?.all { it == 1 || it == -1 }
        val allOpen = doorStatus?.all { it == 0 }
        if (allNotOpen == true) {
            mStatusText.postValue(R.string.error_all_doors_not_open)
        } else if (allOpen == false) {
            mStatusText.postValue(R.string.error_some_doors_not_open)
        }
        if (allOpen == true) {
            showStatusText.postValue(false)
        }
    }

    fun openLocker(lockerId: String) {
        val request = HardwareControllerRequest(
            lockerIds = listOf(lockerId),
            userHandler = PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
            openType = 2
        )
        ioScope.launch {
            mLoading.postValue(true)
            hardwareControllerRepository.openMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        _categoriesRetrieve.value?.map { categories ->
                            categories.modelRetrievies.map { model ->
                                model.lockers.map { locker ->
                                    if (locker.lockerId == lockerId) {
                                        locker.doorStatus = data.locker_list[0].doorStatus
                                        if (data.locker_list[0].doorStatus == 1 || data.locker_list[0].doorStatus == -1) {
                                            mStatusText.postValue(R.string.error_open_failed)
                                            uiScope.launch {
                                                retrieveListener?.openLockerSuccess()
                                            }
                                        } else {
                                            listSerialRetrieve.postValue(listOf(locker.serialNumber))
                                            showStatusText.postValue(false)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun retrieveItem(){
        ioScope.launch {
            mLoading.postValue(true)
            val retrieveItemRequest = RetrieveItemRequest(
                serialNumbers = listSerialRetrieve.value
            )
            managerRepository.retrieveItem(retrieveItemRequest).apply {
                if (isSuccessful) {
                    if (data != null) {
                        _categoriesRetrieve.value?.map { categories ->
                            categories.modelRetrievies.map { model ->
                                model.lockers.map { locker ->
                                    data.lockerRetrieves.find { it == locker.lockerId }?.let {
                                        locker.retrieveStatus = 1
                                    }
                                }
                            }
                        }
                        val isAllRetrieve = _categoriesRetrieve.value?.flatMap { it.modelRetrievies }?.flatMap { it.lockers }?.all { it.retrieveStatus == 1 }
                        uiScope.launch {
                            retrieveListener?.openLockerSuccess()
                            if (isAllRetrieve == true) retrieveListener?.allRetrieveSuccess()
                        }
                    }
                } else handleError(if (status == "422") "60509" else status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}