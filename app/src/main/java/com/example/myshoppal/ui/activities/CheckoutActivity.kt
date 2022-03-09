package com.example.myshoppal.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.CartItem
import com.example.myshoppal.models.Order
import com.example.myshoppal.models.Product
import com.example.myshoppal.ui.adapters.CartItemListAdapter
import com.example.myshoppal.ui.fragments.activities.BaseActivity
import com.example.myshoppal.ui.fragments.activities.DashboardActivity
import com.example.myshoppal.utils.Constants
import com.myshoppal.models.Address
import kotlinx.android.synthetic.main.activity_add_edit_address.*
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity() {

    private var mAddressDetails:Address? = null
    private lateinit var mProductList:ArrayList<Product>
    private lateinit var mCartList:ArrayList<CartItem>
    private var mSubTotal: Double = 0.0
    private var mTotaAmount:Double = 0.0
    private lateinit var mOrderDetails:Order


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails = intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if (mAddressDetails != null){
            tv_checkout_address_type.text = mAddressDetails?.type
            tv_checkout_full_name.text = mAddressDetails?.name
            tv_checkout_address.text= "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            tv_checkout_additional_note.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()){
                tv_checkout_other_details.text = mAddressDetails?.otherDetails
            }

            tv_checkout_mobile_number.text = mAddressDetails?.mobileNumber

        }

        getProductList()

        btn_place_order.setOnClickListener {
            placeAnOrder()

        }

    }

    fun allDetailsUpdatedSuccessfully(){
        hideProgressDialog()
        Toast.makeText(this, "Your order was placed successfully.",Toast.LENGTH_SHORT).show()

        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    fun orderPlacedSuccess(){
        FirestoreClass().updateAllDetails(this, mCartList, mOrderDetails)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_checkout_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProductsList(this)
    }

    fun successProductsListFromFireStore(productList:ArrayList<Product>){
        mProductList = productList

       getCartItemsList()
    }

    private fun getCartItemsList(){
        FirestoreClass().getCartList(this)
    }

    private fun placeAnOrder(){
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mAddressDetails != null){

            mOrderDetails = Order(
                FirestoreClass().getCurrentUserID(),
                mCartList,
                mAddressDetails!!,
                "My order ${System.currentTimeMillis()}",
                mCartList[0].image,
                mTotaAmount.toString(),
                "10.0",
                mTotaAmount.toString(),
                System.currentTimeMillis()
            )

            FirestoreClass().placeOrder(this, mOrderDetails)
        }



    }


    fun successCartItemsList(cartList:ArrayList<CartItem>){
        hideProgressDialog()

        for (product in mProductList){
            for (cart in cartList){
                if (product.product_id == cart.product_id){
                    cart.stock_quantity = product.stock_quantity
                }
            }
        }
        mCartList = cartList

        rv_cart_list_items.layoutManager = LinearLayoutManager(this)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = CartItemListAdapter(this, mCartList, false)
        rv_cart_list_items.adapter = cartListAdapter


        for (item in mCartList){
            val availableQuantity = item.stock_quantity.toInt()
            if (availableQuantity > 0){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)
            }
        }

        tv_checkout_sub_total.text = "$${mSubTotal}"
        tv_checkout_shipping_charge.text = "$10"


        if (mSubTotal > 0){
            ll_checkout_place_order.visibility = View.VISIBLE

            mTotaAmount = mSubTotal + 10.0
            tv_checkout_total_amount.text = "$$mTotaAmount"
        }else{
            ll_checkout_place_order.visibility = View.GONE
        }

    }
}