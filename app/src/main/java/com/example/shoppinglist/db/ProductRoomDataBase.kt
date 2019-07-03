package com.example.shoppinglist.db

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.shoppinglist.ProductDomain
import java.util.concurrent.Executors

@Database(entities = [ProductDomain::class], version = 1)
abstract class ProductRoomDataBase: RoomDatabase() {
    abstract fun productDao(): ProductDao


    companion object {
        private var INSTANCE: ProductRoomDataBase? = null
        var PRODUCT: ArrayList<ProductDomain>? = null

        fun getInstance(context: Context): ProductRoomDataBase? {

            if (INSTANCE == null) {
                synchronized(ProductRoomDataBase) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ProductRoomDataBase::class.java, "Monument_DB"
                    )
                        .allowMainThreadQueries()
                        .addCallback(object : Callback() {
                            override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                Executors.newSingleThreadExecutor()
                                    .execute { PRODUCT?.let { getInstance(context)?.productDao()?.insertAll(it) } }
                            }
                        }).allowMainThreadQueries()
                        .build()
                }

            }
            return INSTANCE
        }

    }
}