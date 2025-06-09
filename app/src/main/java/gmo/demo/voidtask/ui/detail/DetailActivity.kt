package gmo.demo.voidtask.ui.detail

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import gmo.demo.voidtask.R
import gmo.demo.voidtask.ui.base.BaseAppCompatActivity

class DetailActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val productId = intent.getStringExtra("product_id") ?: return

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.detail_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.app_navigation)
        val bundle = bundleOf("product_id" to productId)

        navGraph.setStartDestination(R.id.detailFragment)
        navController.setGraph(navGraph, bundle)
    }
}
