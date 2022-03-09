package com.example.myshoppal.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.databinding.FragmentProductsBinding
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.Product
import com.example.myshoppal.ui.activities.AddProductActivity
import com.example.myshoppal.ui.adapters.MyProductsListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {

    //private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentProductsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {

        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    fun deleteProduct(productID: String){
        showAlertDialogToDeleteProduct(productID)
    }

    fun productDeleteSuccess(){
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()


        getProductListFromFireStore()
    }

    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->


            // START
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteProduct(this@ProductsFragment, productID)
            // END

            dialogInterface.dismiss()
        }

        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    fun successProductsListFromFireStore(productList: ArrayList<Product>){
        hideProgressDialog()

        if (productList.size >0){

            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            val adapterProducts = MyProductsListAdapter(requireActivity(), productList, this)

            rv_my_product_items.adapter = adapterProducts

        }else{
            rv_my_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE

        }

    }

    private fun getProductListFromFireStore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_product ->{
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}