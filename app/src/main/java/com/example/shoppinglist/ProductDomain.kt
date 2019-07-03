package com.example.shoppinglist

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "Product_Table")
class ProductDomain(
    @ColumnInfo(name = "NombreProducto")
    @NotNull
    val nameProduct: String,
    @ColumnInfo(name ="Data" )
    val dataProduct : String
): Serializable{
    @ColumnInfo(name = "ID")
    @Nullable
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
    @ColumnInfo(name = "CantidadProducto")
    var countProduct: Int = 0
    @ColumnInfo(name = "StockProducto")
    var stockProduct : Boolean =false

}