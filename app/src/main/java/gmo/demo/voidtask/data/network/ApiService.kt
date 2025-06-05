package gmo.demo.voidtask.data.network

import com.google.gson.GsonBuilder
import gmo.demo.voidtask.data.models.Product
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    fun getListProducts(@Query("productId") productId: Int): Call<List<Product>>

    companion object {
        private const val BASE_URL =  "https://fakestoreapi.com/"

        val apiService: ApiService by lazy {
            val gson = GsonBuilder().create()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        }
    }

    @GET("products/{id}")
    fun getProductById(@Path("id") id: String): Call<Product>

} 