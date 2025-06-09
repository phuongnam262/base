package gmo.demo.voidtask.ui.productlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gmo.demo.voidtask.BR
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.repositories.ProductRepository
import gmo.demo.voidtask.databinding.ActivityProductListBinding
import gmo.demo.voidtask.ui.common.adapter.ProductAdapter
import gmo.demo.voidtask.ui.common.listener.ClickItemListener
import gmo.demo.voidtask.ui.detail.DetailActivity
import gmo.demo.voidtask.ui.base.BaseActivity

class ProductListActivity : BaseActivity<ActivityProductListBinding, ProductListViewModel>() {

    private lateinit var recProduct: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val mListProduct = mutableListOf<Product>()

    private val repository = ProductRepository()
    override val viewModel: ProductListViewModel by viewModels {
        ProductListViewModelFactory(productId = 1, repository = repository)
    }

    override val layoutId: Int
        get() = R.layout.activity_product_list

    override val bindingVariable: Int
        get() = BR.viewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        recProduct = mViewDataBinding?.recProduct!!
        recProduct.layoutManager = GridLayoutManager(this, 2)
        recProduct.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        productAdapter = ProductAdapter(mListProduct, object : ClickItemListener {
            override fun onClickItem(product: Product) {
                onClickGoToDetail(product)
            }
        })

        recProduct.adapter = productAdapter
    }

    private fun observeViewModel() {
        showLoading()
        viewModel.productList.observe(this) { products ->
            hideLoading()
            mListProduct.clear()
            mListProduct.addAll(products)
            productAdapter.notifyDataSetChanged()
        }

        viewModel.error.observe(this) { errorMsg ->
            hideLoading()
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickGoToDetail(product: Product) {
        startActivityWithOneValue("product_id", product.id.toString(), DetailActivity::class.java)
    }
}


