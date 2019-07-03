package com.example.shoppinglist

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.db.ProductViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_product_dialog.view.*
import kotlinx.android.synthetic.main.toolbar.*
import android.view.Menu as Menu1
import com.example.shoppinglist.ListOfProducts as ListOfProducts1

@Suppress("DEPRECATION")
open class ListOfProducts : AppCompatActivity() {


    private lateinit var productsViewModel: ProductViewModel
    private var productsList:ArrayList<ProductDomain>?=null
    private lateinit var searchView:SearchView
    var adapter = ProductAdapter()


    @SuppressLint("RestrictedApi", "InflateParams", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title="List the Buy"

        val recyclerView = recycler_list_id
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter=adapter

        productsViewModel= ViewModelProviders.of(this).get(ProductViewModel::class.java)
        productsViewModel.getAllProduct()?.observe(this, Observer {
                productLis ->
            run {
                productLis?.let {
                adapter.setData(productLis)
                this.productsList = productLis as ArrayList<ProductDomain>?
                }
            }
        })
        productsList=productsViewModel.getAllProduct2() as ArrayList<ProductDomain>

        fab_list_products.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_product_dialog,null)
            val mBilder= AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog=mBilder.show()

            mDialogView.btn_add_product.setOnClickListener {
                mAlertDialog.dismiss()
                val nameProducts = mDialogView.name_product_add.text.toString()
                val dataProducts=mDialogView.data_product_add.text.toString()
                val product = ProductDomain(nameProducts,dataProducts)
               comparator(product, productsViewModel.getAllProduct2()!!)

            }
        }
    }
    private fun comparator(productFun: ProductDomain, productFunList: ArrayList<ProductDomain>){
        if (productFun.nameProduct.isEmpty() || productFun.dataProduct.isEmpty()) {
            Toast.makeText(this, "Error al Introducir Producto", Toast.LENGTH_LONG).show()

        } else {
            if (productFunList.find { it.nameProduct == productFun.nameProduct} == null) {
                productsViewModel.addProduct(productFun)
            } else  {
                Toast.makeText(this, "El Producto ya Registrado", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu1?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_activity,menu)

        val searchManager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu!!.findItem(R.id.search_id_menu).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth= Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.submit_id_menu->sendEmail()
            R.id.delete_id_menu-> productsList?.let { deleteProduct(it) }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed(){
        if(!searchView.isIconified){
            searchView.isIconified=true
            return
        }
        super.onBackPressed()
    }
    private fun sendEmail() {

            val orderString= productsList?.let { submitString(it) }
            val orderIntent = Intent(Intent.ACTION_SEND)
            orderIntent.data = Uri.parse("mailto:")
            orderIntent.type = "text/plain"
            orderIntent.putExtra(Intent.EXTRA_TEXT, orderString)
            try {
                startActivity(Intent.createChooser(orderIntent, "Enviar pedido..."))
                finish()
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    private fun submitString(listArray: ArrayList<ProductDomain>):String {
        var order = ""
        for (product in listArray) if (product.stockProduct) order+=product.countProduct.toString()+" "+product.dataProduct+" de "+product.nameProduct+"\n"

        return order
    }
    private fun deleteProduct(listArray: ArrayList<ProductDomain>) {
        for (product in listArray) if (product.stockProduct){
            productsViewModel.deleteProduct(product) }
    }
}
