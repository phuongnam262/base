package gmo.demo.voidtask.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.network.ApiService
import gmo.demo.voidtask.databinding.ActivityDetailBinding
import gmo.demo.voidtask.ui.common.adapter.OtherProductsAdapter
import gmo.demo.voidtask.ui.common.listener.ClickItemListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var recOtherProducts: RecyclerView
    private var currentQuantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("object_product") as? Product ?: run {
            Toast.makeText(this, "Product data not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupAddandRemoveQuantity()
        displayProductDetails(product)
        setupOtherProductsRecyclerView(product.id)
    }

    private fun setupAddandRemoveQuantity() {
        binding.btnDecrease.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                binding.tvQuantity.text = currentQuantity.toString()
            }
        }

        binding.btnIncrease.setOnClickListener {
            if (currentQuantity < 10) {
                currentQuantity++
                binding.tvQuantity.text = currentQuantity.toString()
            }
        }
    }

    private fun displayProductDetails(product: Product) {
        binding.tvDetailTitle.text = product.title
        binding.tvDetailPrice.text = "$" + product.price.toString()
        binding.tvDetailDescription.text = product.description
        Glide.with(this).load(product.image).into(binding.imgDetailProduct)

        binding.btnAddToCart.setOnClickListener {
            Toast.makeText(this, "Added $currentQuantity of ${product.title} to cart", Toast.LENGTH_SHORT).show()
            // Add actual add to cart logic here
        }
    }

    private fun setupOtherProductsRecyclerView(currentProductId: String?) {
        // Note: Calling getListProducts(productId = 1) might not be the correct way to get *other* products.
        // The fakestoreapi /products endpoint without a parameter usually returns all products.
        // If the API requires a parameter, this call might return only product with ID 1, or fail.
        // Adjust the API call here if needed to fetch a list of products to filter from.
        ApiService.apiService.getListProducts(productId = 1).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                val allOtherProducts = response.body() ?: emptyList()
                Log.d("DetailActivity", "Fetched other products: \${allOtherProducts.size}")

                // Lọc và lấy 5 sản phẩm ngẫu nhiên khác
                val otherRandomProducts = allOtherProducts
                    .filter { it.id.toString() != currentProductId.toString() }
                    .shuffled()
                    .take(5)

                Log.d("DetailActivity", "Filtered other products: \${otherRandomProducts.size}")
                setupRecyclerView(otherRandomProducts)
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Error loading other products: \${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("DetailActivity", "Error loading other products", t)
            }
        })
    }

    private fun setupRecyclerView(products: List<Product>) {
        recOtherProducts = binding.recOtherProducts
        recOtherProducts.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        recOtherProducts.adapter = OtherProductsAdapter(products, object : ClickItemListener {
            override fun onClickItem(product: Product) {
                val intent = Intent(this@DetailActivity, DetailActivity::class.java).apply {
                    putExtra("object_product", product)
                }
                startActivity(intent)
            }
        })
    }
} 