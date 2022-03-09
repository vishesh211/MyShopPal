package com.example.myshoppal.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.User
import com.example.myshoppal.ui.fragments.activities.BaseActivity
import com.example.myshoppal.ui.fragments.activities.LoginActivity
import com.example.myshoppal.ui.fragments.activities.UserProfileActivity
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.toolbar_forgot_password_activity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener{

    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupActionBar()

        tv_edit.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
        ll_address.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().userDetails(this)
    }

    fun userDetailsSuccess(user: User){
        hideProgressDialog()

        mUserDetails = user

        GlideLoader(this).loadUserPicture(user.image, iv_user_photo)

        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_email.text = user.email
        tv_gender.text = user.gender
        tv_mobile_number.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id) {
                R.id.btn_logout ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
            }

                R.id.tv_edit ->{
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)

                }

                R.id.ll_address ->{
                    val intent = Intent(this, AddressListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}