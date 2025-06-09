package gmo.demo.voidtask.ui.detail

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import gmo.demo.voidtask.BR
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.repositories.ProductRepository
import gmo.demo.voidtask.databinding.ActivityDetailBinding
import gmo.demo.voidtask.ui.base.BaseActivity

class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>() {

    private val repository = ProductRepository()

    override val layoutId: Int
        get() = R.layout.activity_detail

    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: DetailViewModel by lazy {
        val productId = intent.getStringExtra("product_id") ?: ""
        val factory = DetailViewModelFactory(productId, repository)
        ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoading()
        
        val productId = intent.getStringExtra("product_id") ?: return

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.detail_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.app_navigation)
        val bundle = bundleOf("product_id" to productId)

        navGraph.setStartDestination(R.id.detailFragment)
        navController.setGraph(navGraph, bundle)
        
        hideLoading()
    }
}
