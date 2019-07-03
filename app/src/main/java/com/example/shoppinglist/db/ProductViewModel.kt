package com.example.shoppinglist.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.shoppinglist.ProductDomain
import java.io.Serializable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProductViewModel (application: Application): AndroidViewModel(application), Serializable {

    private var productDao: ProductDao? = null
    private var executorService : ExecutorService?=null

    init {
        productDao = ProductRoomDataBase.getInstance(application)?.productDao()
        executorService = Executors.newSingleThreadExecutor()
    }
    internal fun getAllProduct(): LiveData<List<ProductDomain>>? {
        return productDao?.getAllProduct()
    }
    internal fun addProduct(addProduct: ProductDomain) {
        executorService?.execute {productDao?.insert(addProduct) }
    }
    internal fun deleteProduct(deleteProduct: ProductDomain){
        return productDao?.delete(deleteProduct)!!
    }

    internal fun getAllProduct2(): ArrayList<ProductDomain>? {
        return productDao?.getAllProductList() as ArrayList<ProductDomain>?
    }


}