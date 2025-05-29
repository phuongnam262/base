package gmo.demo.voidtask.data.models

import java.io.Serializable

data class Product(
    val id: String ? =null,
    val title: String ? =null,
    val price: String ? =null,
    val image: String ? =null,
    val description: String ? =null
): Serializable