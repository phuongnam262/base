package gmo.demo.voidtask.ui.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListViewModel : ViewModel() {

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> = _productList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getProducts(productId: Int = 1) {
        ApiService.apiService.getListProducts(productId).enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful) {
                    _productList.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load products"
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                _error.value = "onFailure: ${t.message}"
                // t.printStackTrace() // optional debug log
            }
        })
    }
}
