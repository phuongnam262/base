package gmo.demo.voidtask.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.models.Product
import gmo.demo.voidtask.ui.common.listener.ClickItemListener

// Adapter to display other products, typically in a horizontal list
// This is similar to ProductAdapter but might use a different layout item
class OtherProductsAdapter(private val mListProduct: List<Product>, private val clickItemListener: ClickItemListener) :
    RecyclerView.Adapter<OtherProductsAdapter.OtherProductViewHolder>() {

    inner class OtherProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduct: ImageView = itemView.findViewById(R.id.img_product)
        val tvProductTitle: TextView = itemView.findViewById(R.id.tv_product_title)
        val tvProductPrice: TextView = itemView.findViewById(R.id.tv_product_price)

        fun bind(product: Product) {
            // Load image using a library like Glide or Picasso
            // Example with Glide:
            Glide.with(itemView.context).load(product.image).into(imgProduct)
            tvProductTitle.text = product.title
            tvProductPrice.text = "$" + product.price

            itemView.setOnClickListener {
                clickItemListener.onClickItem(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherProductViewHolder {
        // Use the same item layout as ProductAdapter for now, adjust if needed
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return OtherProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: OtherProductViewHolder, position: Int) {
        val product = mListProduct[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return mListProduct.size
    }
} 