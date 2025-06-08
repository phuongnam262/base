package gmo.demo.voidtask.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.repositories.ProductRepository
import gmo.demo.voidtask.ui.base.BaseViewModel

class DetailViewModel(
    private val productId: String,
    private val repository: ProductRepository
) : BaseViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        fetchProductDetails()
    }

    private fun fetchProductDetails() {
        repository.getProductById(productId).observeForever { result ->
            result.onSuccess {
                _product.value = it
            }.onFailure {
                _error.value = it.message ?: "Unknown error"
            }
        }
    }
}
