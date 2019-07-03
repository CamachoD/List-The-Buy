package com.example.shoppinglist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_product_list.view.*

@Suppress("DEPRECATION")
class ProductAdapter internal constructor() :
    RecyclerView.Adapter<ProductAdapter.ViewHolderProduct>(),Filterable{



    private var productListAdapter = ArrayList<ProductDomain>()
    private var selectedProductList = ArrayList<Long>()
    private var filterList=productListAdapter


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProduct {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_product_list, null, false)
        return ViewHolderProduct(view)
    }

    override fun getItemCount(): Int { return filterList.size }

    override fun onBindViewHolder(holder: ViewHolderProduct, position: Int) {
        with(holder) {
            nameProduct.text = filterList[position].nameProduct
            checkBoxProduct.isChecked = filterList[position].stockProduct
            countProduct.text = filterList[position].countProduct.toString()
            dataProducts.text=filterList[position].dataProduct

            if (selectedProductList.contains(filterList[position].id)) {
                itemView.id_seekbar_count.visibility = View.VISIBLE
                itemView.label_count_product.visibility = View.VISIBLE
                itemView.label_data_product.visibility = View.VISIBLE
                itemView.checkBox_product_card.isChecked = true
                itemView.id_seekbar_count.progress = filterList[position].countProduct
                productListAdapter[position].stockProduct = true
            } else {
                itemView.checkBox_product_card.isChecked = false
                itemView.id_seekbar_count.visibility = View.GONE
                itemView.label_count_product.visibility = View.GONE
                itemView.label_data_product.visibility = View.GONE
                filterList[position].stockProduct = false
            }

            with(itemView) {
                checkBox_product_card.setOnClickListener {
                    if (selectedProductList.contains(filterList[position].id)) {
                        selectedProductList.remove(filterList[position].id)
                    } else {
                        selectedProductList.add(filterList[position].id)
                    }
                    notifyDataSetChanged()
                }

                id_seekbar_count.max = 25
                id_seekbar_count.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        label_count_product.text = progress.toString()
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        filterList[position].countProduct = if (seekBar?.progress != null) seekBar.progress else 0
                    }
                })

            }
        }
    }

    class ViewHolderProduct(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameProduct: TextView = itemView.id_name_product_card
        var checkBoxProduct: CheckBox = itemView.checkBox_product_card
        val countProduct: TextView = itemView.label_count_product
        val dataProducts:TextView=itemView.label_data_product
    }

    fun setData(newData: List<ProductDomain>) {
        val postDiffCallback = PostDiffCallback(productListAdapter, newData)
        val diffResult = DiffUtil.calculateDiff(postDiffCallback)

        productListAdapter.clear()
        productListAdapter.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    internal inner class PostDiffCallback(
        private val oldProduct: List<ProductDomain>,
        private val newProduct: List<ProductDomain>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int { return oldProduct.size }

        override fun getNewListSize(): Int { return newProduct.size }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean { return oldProduct[oldItemPosition].id === newProduct[newItemPosition].id }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean { return oldProduct[oldItemPosition] == newProduct[newItemPosition] }
    }

    override fun getFilter(): Filter {

        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filterList = if (charSearch.isEmpty()){
                    productListAdapter
                }else{
                    val resultList=ArrayList<ProductDomain>()
                    for(row in productListAdapter){
                        if (row.nameProduct.toLowerCase().contains(charSearch.toLowerCase()))
                            resultList.add(row)
                    }
                    resultList
                }
                val filterResults= FilterResults()
                filterResults.values=filterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
               filterList= results!!.values as ArrayList<ProductDomain>
                notifyDataSetChanged()
            }
        }
    }
}


