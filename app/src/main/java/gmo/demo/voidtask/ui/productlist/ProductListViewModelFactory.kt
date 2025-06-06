package gmo.demo.voidtask.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gmo.demo.voidtask.data.repositories.ProductRepository

class ProductListViewModelFactory(
    private val productId: Int,
    private val repository: ProductRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            return ProductListViewModel(productId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

