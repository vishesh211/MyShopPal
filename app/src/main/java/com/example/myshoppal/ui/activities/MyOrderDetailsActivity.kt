package com.example.myshoppal.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.models.Order
import com.example.myshoppal.ui.adapters.CartItemListAdapter
import com.example.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_my_order_details.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.toolbar_user_profile_activity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyOrderDetailsActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order_details)

        setupActionBar()

        var myOrderDetails:Order = Order()

        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)){
            myOrderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }

        setUpUI(myOrderDetails)

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_my_order_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_my_order_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setUpUI(orderDetails: Order){

        tv_order_details_id.text = orderDetails.title

        val dateFormat = "dd MM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        tv_order_details_date.text = orderDateTime


        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        when{

            diffInHours < 1 ->{
                tv_order_status.text = resources.getString(R.string.order_status_pending)
                tv_order_status.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            }

            diffInHours < 2 ->{
                tv_order_status.text = resources.getString(R.string.order_status_in_process)
                tv_order_status.setTextColor(ContextCompat.getColor(this, R.color.colorOrderStatusInProcess))
            }

            else ->{
                tv_order_status.text = resources.getString(R.string.order_status_delivered)
                tv_order_status.setTextColor(ContextCompat.getColor(this, R.color.colorOrderStatusDelivered))
            }

        }

        rv_my_order_items_list.layoutManager = LinearLayoutManager(this)
        rv_my_order_items_list.setHasFixedSize(true)

        val adapter = CartItemListAdapter(this, orderDetails.items, false)
        rv_my_order_items_list.adapter = adapter


        tv_my_order_details_address_type.text = orderDetails.address.type
        tv_my_order_details_full_name.text = orderDetails.address.name
        tv_my_order_details_address.text = "${orderDetails.address.address}  ${orderDetails.address.zipCode}"

        tv_my_order_details_additional_note.text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetails.isNotEmpty()){
            tv_my_order_details_other_details.visibility = View.VISIBLE
            tv_my_order_details_other_details.text = orderDetails.address.otherDetails
        }else{
            tv_my_order_details_other_details.visibility = View.GONE
        }
        tv_my_order_details_mobile_number.text = orderDetails.address.mobileNumber

        tv_order_details_sub_total.text = orderDetails.sub_total_amount
        tv_order_details_shipping_charge.text = orderDetails.total_amount
        tv_order_details_total_amount.text = orderDetails.total_amount



    }
}