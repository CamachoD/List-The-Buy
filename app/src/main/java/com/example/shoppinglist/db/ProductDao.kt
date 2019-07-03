package com.example.shoppinglist.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shoppinglist.ProductDomain
const val select: String ="SELECT ID,CantidadProducto,Data,NombreProducto,StockProducto From Product_Table"

@Dao
interface ProductDao {

    @Query(select)
    fun getAllProduct(): LiveData<List<ProductDomain>>

    @Query(select)
    fun getAllProductList(): List<ProductDomain>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(productInsertList: List<ProductDomain>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productInsert: ProductDomain)

    @Delete
    fun delete(productDelete: ProductDomain)
}