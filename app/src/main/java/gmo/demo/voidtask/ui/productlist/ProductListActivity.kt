package gmo.demo.voidtask.ui.productlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.network.ApiService
import gmo.demo.voidtask.ui.common.adapter.ProductAdapter
import gmo.demo.voidtask.ui.common.listener.ClickItemListener
import gmo.demo.voidtask.ui.detail.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class ProductListActivity : AppCompatActivity() {

    private lateinit var recProduct: RecyclerView
    private lateinit var mListProduct: MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        recProduct = findViewById(R.id.rec_product)
        recProduct.layoutManager = GridLayoutManager(this, 2)

        mListProduct = ArrayList()

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recProduct.addItemDecoration(itemDecoration)

        callApiGetProducts()
    }

    private fun callApiGetProducts() {
        ApiService.apiService.getListProducts(productId = 1).enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                val productList = response.body() ?: emptyList()
                mListProduct.clear()
                mListProduct.addAll(productList)
                val productAdapter = ProductAdapter(mListProduct, object : ClickItemListener {
                    override fun onClickItem(product: Product) {
                        onClickGoToDetail(product)
                    }
                })
                recProduct.adapter = productAdapter
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ProductListActivity, "onFailure", Toast.LENGTH_SHORT).show()
                // Log the error for debugging
                t.printStackTrace()
            }
        })
    }

    private fun onClickGoToDetail(product: Product) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        // Ensure Product is Serializable or Parcelable
        bundle.putSerializable("object_product", product)
        intent.putExtras(bundle)
        startActivity(intent)
    }
} 