package gmo.demo.voidtask.ui.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.repositories.ProductRepository
import gmo.demo.voidtask.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productId: Int,
    private val repository: ProductRepository
) : BaseViewModel() {

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> = _productList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            try {
                mLoading.value = true
                repository.getProducts(productId).observeForever { result ->
                    result.onSuccess {
                        _productList.value = it
                    }.onFailure {
                        _error.value = "Error: ${it.message}"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                mLoading.value = false
            }
        }
    }
}
