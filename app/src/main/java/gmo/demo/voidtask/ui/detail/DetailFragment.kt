package gmo.demo.voidtask.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gmo.demo.voidtask.R
import gmo.demo.voidtask.BR
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.data.network.ApiService
import gmo.demo.voidtask.data.repositories.ProductRepository
import gmo.demo.voidtask.databinding.FragmentDetailBinding
import gmo.demo.voidtask.ui.base.BaseFragment
import gmo.demo.voidtask.ui.common.adapter.OtherProductsAdapter
import gmo.demo.voidtask.ui.common.listener.ClickItemListener
import gmo.demo.voidtask.ui.myCart.CartActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>() {

    override val layoutId: Int get() = R.layout.fragment_detail
    override val bindingVariable: Int get() = BR.viewModel

    override val viewModel: DetailViewModel by lazy {
        val id = arguments?.getString("product_id") ?: ""
        ViewModelProvider(
            this,
            DetailViewModelFactory(id, ProductRepository())
        )[DetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments?.getString("product_id")?.isEmpty() == true) {
            Toast.makeText(requireContext(), "Product ID not found", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.product.observe(viewLifecycleOwner) { product ->
            mViewDataBinding?.apply {
                tvDetailTitle.text = product.title
                tvDetailPrice.text = "$${product.price}"
                tvDetailDescription.text = product.description
                Glide.with(requireContext()).load(product.image).into(imgDetailProduct)

                btnAddToCart.setOnClickListener {
                    addToCart(product)
                }
            }

            setupOtherProductsRecyclerView(product.id)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        setupAddRemoveQuantity()
    }

    private var currentQuantity = 1
    private fun setupAddRemoveQuantity() {
        mViewDataBinding?.apply {
            btnDecrease.setOnClickListener {
                if (currentQuantity > 1) {
                    currentQuantity--
                    tvQuantity.text = currentQuantity.toString()
                }
            }
            btnIncrease.setOnClickListener {
                if (currentQuantity < 10) {
                    currentQuantity++
                    tvQuantity.text = currentQuantity.toString()
                }
            }
        }
    }

    private fun addToCart(product: Product) {
        // Thêm sản phẩm vào giỏ hàng
        gmo.demo.voidtask.data.repositories.CartManager.addToCart(product, currentQuantity)
        
        Toast.makeText(requireContext(), getString(R.string.added_to_cart, product.title), Toast.LENGTH_SHORT).show()
        
        // Chuyển đến màn hình giỏ hàng
//        val intent = Intent(requireContext(), CartActivity::class.java)
//        startActivity(intent)
    }

    private fun setupOtherProductsRecyclerView(currentProductId: String?) {
        ApiService.apiService.getListProducts(1).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                val otherProducts = response.body()?.filter { it.id.toString() != currentProductId }?.shuffled()?.take(5)
                if (otherProducts != null) {
                    val adapter = OtherProductsAdapter(otherProducts, object : ClickItemListener {
                        override fun onClickItem(product: Product) {
                            val intent = Intent(requireContext(), DetailActivity::class.java)
                            intent.putExtra("product_id", product.id.toString())
                            startActivity(intent)
                        }
                    })
                    mViewDataBinding?.recOtherProducts?.apply {
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        this.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to load other products", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



