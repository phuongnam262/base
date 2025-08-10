package gmo.demo.voidtask.ui.myCart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.models.CartItem
import gmo.demo.voidtask.databinding.ItemCartBinding
import java.text.SimpleDateFormat
import java.util.Locale

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onQuantityChanged: (CartItem, Int) -> Unit,
    private val onRemoveItem: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(cartItem: CartItem) {
            binding.apply {
                tvProductName.text = cartItem.productName
                tvProductPrice.text = cartItem.price // Remove $ since price is now a string
                tvQuantity.text = cartItem.quantity.toString()
                tvTotalPrice.text = "$${String.format("%.2f", cartItem.totalPrice)}"
                tvOrderTime.text = itemView.context.getString(
                    R.string.order_time, 
                    dateFormat.format(cartItem.orderTime)
                )
                
                // Load ảnh sản phẩm
                Glide.with(itemView.context)
                    .load(cartItem.image)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imgProduct)
                
                // Xử lý nút tăng/giảm số lượng
                btnIncrease.setOnClickListener {
                    onQuantityChanged(cartItem, cartItem.quantity + 1)
                }
                
                btnDecrease.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        onQuantityChanged(cartItem, cartItem.quantity - 1)
                    }
                }
                
                // Xử lý nút xóa
                btnRemove.setOnClickListener {
                    onRemoveItem(cartItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size
}
