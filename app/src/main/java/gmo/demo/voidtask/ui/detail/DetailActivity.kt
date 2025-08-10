package gmo.demo.voidtask.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import gmo.demo.voidtask.R
import gmo.demo.voidtask.databinding.ActivityDetailBinding
import gmo.demo.voidtask.ui.productlist.ProductListActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        if (savedInstanceState == null) {
            val productId = intent.getStringExtra("product_id")
            if (productId != null) {
                val fragment = DetailFragment().apply {
                    arguments = Bundle().apply {
                        putString("product_id", productId)
                    }
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit()
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, ProductListActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
        return true
    }
}
