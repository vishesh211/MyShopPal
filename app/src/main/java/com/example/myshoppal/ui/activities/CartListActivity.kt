package com.example.myshoppal.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.CartItem
import com.example.myshoppal.models.Product
import com.example.myshoppal.ui.adapters.CartItemListAdapter
import com.example.myshoppal.ui.fragments.activities.BaseActivity
import com.example.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_product_details.toolbar_product_details_activity

class CartListActivity : BaseActivity() {

    private lateinit var mProductsList:ArrayList<Product>
    private lateinit var mCartListItems:ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setUpActionBar()

        btn_checkout.setOnClickListener {
            val intent = Intent(this, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()

        getProductsList()
    }


    fun itemUpdateSuccess(){
        hideProgressDialog()
        getCartItemsList()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>){
        hideProgressDialog()

        for (product in mProductsList){
            for (cartItem in cartList){
                if (product.product_id == cartItem.product_id){
                    cartItem.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0){
                        cartItem.cart_quantity = product.stock_quantity
                    }

                }
            }
        }

        mCartListItems = cartList

        if (mCartListItems.size > 0){
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this)
            rv_cart_items_list.setHasFixedSize(true)

            val cardListAdapter = CartItemListAdapter(this, mCartListItems, true)

            rv_cart_items_list.adapter = cardListAdapter

            var subtotal:Double = 0.0

            for (i in mCartListItems){

                val availableQuantity = i.stock_quantity.toInt()

                if (availableQuantity > 0){
                    val price = i.price.toDouble()
                    val quantity = i.cart_quantity.toDouble()
                    subtotal+=(price * quantity)
                }
            }
            tv_sub_total.text = "$${subtotal}"
            tv_shipping_charge.text = "$10.0" // TODO Change shipping charge logic

            if (subtotal >0){
                ll_checkout.visibility = View.VISIBLE

                val total = subtotal + 10 //TODO - Change logic here

                tv_total_amount.text = "$$total"
            }else{
                ll_checkout.visibility = View.VISIBLE
            }

        }else{
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>){
        hideProgressDialog()
        mProductsList = productsList

        getCartItemsList()
    }

    private fun getProductsList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this)
    }

    private fun getCartItemsList(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this)
    }

    fun itemRemovedSuccess() {
        hideProgressDialog()

        Toast.makeText(this, resources.getString(R.string.msg_item_removed_successfully), Toast.LENGTH_SHORT).show()

        getCartItemsList()
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}