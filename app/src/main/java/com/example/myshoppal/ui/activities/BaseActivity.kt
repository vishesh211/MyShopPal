package com.example.myshoppal.ui.fragments.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    private var doubleBackToExitPressesOnce: Boolean = false

    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)

        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSnackBarError))
        }else{
            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSnackBarSuccess))
        }

        snackBar.show()
    }

    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressesOnce){
            onBackPressed()
            finish()
        }

        doubleBackToExitPressesOnce = true

        Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressesOnce = false
        }, 2000)
    }

}