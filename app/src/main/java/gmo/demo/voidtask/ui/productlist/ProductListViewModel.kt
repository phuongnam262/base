package gmo.demo.voidtask.ui.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.repositories.ProductRepository

class ProductListViewModel(
    private val productId: Int,
    private val repository: ProductRepository
) : ViewModel() {

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> = _productList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        getProducts()
    }

    private fun getProducts() {
        repository.getProducts(productId).observeForever { result ->
            result.onSuccess {
                _productList.value = it
            }.onFailure {
                _error.value = "Error: ${it.message}"
            }
        }
    }
}
