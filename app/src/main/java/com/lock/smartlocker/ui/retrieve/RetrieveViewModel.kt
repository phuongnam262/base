package com.lock.smartlocker.ui.retrieve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.models.Categories
import com.lock.smartlocker.data.models.Category
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

    private val _categoriesRetrieve = MutableLiveData<List<Categories>>()
    val categoriesRetrieve: LiveData<List<Categories>> get() = _categoriesRetrieve

    private val _retrieveModels = MutableLiveData<List<LockerRetrieve>>()
    val retrieveModels: LiveData<List<LockerRetrieve>> get() = _retrieveModels
    var listLockerId = MutableLiveData<List<String>>()
    var categoryIdSelected = MutableLiveData<String>()

    init {
        getAllItemRetrieve()
    }

    private fun getAllItemRetrieve() {
        ioScope.launch {
            mLoading.postValue(true)
            managerRepository.getAllItemRetrieve().apply {
                if (isSuccessful) {
                    if (data != null) {
                        val listId = ArrayList<String>()
                        _categoriesRetrieve.postValue(data.categories)
                        data.categories.map { categories ->
                            listId.addAll(categories.modelRetrievies.flatMap { model->
                                model.lockers.map { it.lockerId }
                            })
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
        val categoriesItem = _categoriesRetrieve.value?.find { it.categoryId == category.categoryId }
        categoriesItem.let {categories ->
            val listLocker = categories?.modelRetrievies?.flatMap { it.lockers}
            _retrieveModels.postValue(listLocker ?: emptyList())
        }
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
//                        _lockers.value?.map { locker ->
//                            val matchingStatus =
//                                data.locker_list.find { it.lockerId == locker.lockerId }
//                            if (matchingStatus != null) {
//                                locker.doorStatus = matchingStatus.doorStatus
//                            }
//                        }
//                        _lockers.value?.let { checkStatusDoor(it) }
//                        uiScope.launch { manageLockerListener?.openLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
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
//                        _lockers.value?.map { locker ->
//                            val matchingStatus =
//                                data.locker_list.find { it.lockerId == locker.lockerId }
//                            if (matchingStatus != null) {
//                                locker.doorStatus = matchingStatus.doorStatus
//                                if (matchingStatus.doorStatus == 1 || matchingStatus.doorStatus == -1) {
//                                    mStatusText.postValue(R.string.error_open_failed)
//                                }else{
//                                    showStatusText.postValue(false)
//                                }
//                            }
//                        }
//                        uiScope.launch { manageLockerListener?.openLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}