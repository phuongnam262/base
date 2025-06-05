package gmo.demo.voidtask.ui.productlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.ui.common.adapter.ProductAdapter
import gmo.demo.voidtask.ui.common.listener.ClickItemListener
import gmo.demo.voidtask.ui.detail.DetailActivity

class ProductListActivity : AppCompatActivity() {

    private lateinit var recProduct: RecyclerView
    private val viewModel: ProductListViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private val mListProduct = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        recProduct = findViewById(R.id.rec_product)
        recProduct.layoutManager = GridLayoutManager(this, 2)
        recProduct.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        productAdapter = ProductAdapter(mListProduct, object : ClickItemListener {
            override fun onClickItem(product: Product) {
                onClickGoToDetail(product)
            }
        })

        recProduct.adapter = productAdapter

        observeViewModel()
        viewModel.getProducts()
    }

    private fun observeViewModel() {
        viewModel.productList.observe(this, Observer { products ->
            mListProduct.clear()
            mListProduct.addAll(products)
            productAdapter.notifyDataSetChanged()
        })

        viewModel.error.observe(this, Observer { errorMsg ->
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        })
    }

    private fun onClickGoToDetail(product: Product) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("object_product", product)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}
