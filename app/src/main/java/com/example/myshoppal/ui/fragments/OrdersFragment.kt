package com.example.myshoppal.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.databinding.FragmentOrdersBinding
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.Order
import com.example.myshoppal.ui.adapters.MyOrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : BaseFragment() {

   // private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun populateOrdersListInUI(orderList: ArrayList<Order>){
        hideProgressDialog()

        if (orderList.size > 0){
            tv_no_orders_found.visibility = View.GONE
            rv_my_order_items.visibility = View.VISIBLE

            rv_my_order_items.layoutManager = LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val adapter = MyOrdersListAdapter(requireActivity(), orderList)
            rv_my_order_items.adapter = adapter
        }else{
            tv_no_orders_found.visibility = View.VISIBLE
            rv_my_order_items.visibility = View.GONE
        }


    }

    private fun getMyOrdersList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getMyOrdersList(this)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }
}