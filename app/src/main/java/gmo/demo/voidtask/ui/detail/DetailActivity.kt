package gmo.demo.voidtask.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gmo.demo.voidtask.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (savedInstanceState == null) {
            val productId = intent.getStringExtra("product_id")
            val fragment = DetailFragment().apply {
                arguments = Bundle().apply {
                    putString("product_id", productId)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
} 