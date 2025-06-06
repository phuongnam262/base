package gmo.demo.voidtask.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {

    fun getProducts(productId: Int): LiveData<Result<List<Product>>> {
        val result = MutableLiveData<Result<List<Product>>>()

        ApiService.Companion.apiService.getListProducts(productId).enqueue(object :
            Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    result.value = Result.success(response.body() ?: emptyList())
                } else {
                    result.value = Result.failure(Exception("Failed to load products"))
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                result.value = Result.failure(t)
            }
        })

        return result
    }

    fun getProductById(productId: String): LiveData<Result<Product>> {
        val result = MutableLiveData<Result<Product>>()

        ApiService.apiService.getProductById(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.value = Result.success(it)
                    } ?: run {
                        result.value = Result.failure(Exception("Product not found"))
                    }
                } else {
                    result.value = Result.failure(Exception("Failed to load product"))
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                result.value = Result.failure(t)
            }
        })

        return result
    }


}