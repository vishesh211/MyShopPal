package com.example.myshoppal.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.SoldProduct
import com.example.myshoppal.ui.adapters.SoldProductsListAdapter
import kotlinx.android.synthetic.main.fragment_sold_products.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SoldProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoldProductsFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }

    private fun getSoldProductsList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getSoldProductsList(this)
    }

    fun successSoldProductsList(soldProductList:ArrayList<SoldProduct>){
        hideProgressDialog()

        if (soldProductList.size > 0){
            rv_sold_product_items.visibility = View.VISIBLE
            tv_no_sold_products_found.visibility = View.GONE

            rv_sold_product_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_product_items.setHasFixedSize(true)

            val adapter = SoldProductsListAdapter(requireActivity(), soldProductList)
            rv_sold_product_items.adapter = adapter

        }else{
            rv_sold_product_items.visibility = View.GONE
            tv_no_sold_products_found.visibility = View.VISIBLE
        }
    }


}