package gmo.demo.voidtask.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.network.ApiService
import gmo.demo.voidtask.data.repositories.ProductRepository
import gmo.demo.voidtask.databinding.ActivityDetailBinding
import gmo.demo.voidtask.ui.base.BaseAppCompatActivity
import gmo.demo.voidtask.ui.common.adapter.OtherProductsAdapter
import gmo.demo.voidtask.ui.common.listener.ClickItemListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : BaseAppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var recOtherProducts: RecyclerView
    private var currentQuantity = 1

    private val repository = ProductRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getStringExtra("product_id")
        if (productId == null) {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel = ViewModelProvider(
            this,
            DetailViewModelFactory(productId, repository)
        )[DetailViewModel::class.java]

        observeViewModel()
        setupAddRemoveQuantity()
    }

    private fun observeViewModel() {
        viewModel.product.observe(this) { product ->
            displayProductDetails(product)
            setupOtherProductsRecyclerView(product.id)
        }

        viewModel.error.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayProductDetails(product: Product) {
        binding.tvDetailTitle.text = product.title
        binding.tvDetailPrice.text = buildString {
        append("$")
        append(product.price)
    }
        binding.tvDetailDescription.text = product.description
        Glide.with(this).load(product.image).into(binding.imgDetailProduct)

        binding.btnAddToCart.setOnClickListener {
            Toast.makeText(this, "Added $currentQuantity of ${product.title} to cart", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAddRemoveQuantity() {
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

    private fun setupOtherProductsRecyclerView(currentProductId: String?) {
        ApiService.apiService.getListProducts(productId = 1).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                val allOtherProducts = response.body() ?: emptyList()
                val otherRandomProducts = allOtherProducts
                    .filter { it.id.toString() != currentProductId }
                    .shuffled()
                    .take(5)

                setupRecyclerView(otherRandomProducts)
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Error loading other products: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView(products: List<Product>) {
        recOtherProducts = binding.recOtherProducts
        recOtherProducts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recOtherProducts.adapter = OtherProductsAdapter(products, object : ClickItemListener {
            override fun onClickItem(product: Product) {
                startActivityWithOneValue("product_id", product.id.toString(), DetailActivity::class.java)
            }
        })
    }
}

