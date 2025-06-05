package gmo.demo.voidtask.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val productId: String) : ViewModel() {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        fetchProductDetails()
    }

    private fun fetchProductDetails() {
        ApiService.apiService.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    _product.value = response.body()
                } else {
                    _error.value = "Failed to load product"
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                _error.value = t.message
            }
        })
    }
}
