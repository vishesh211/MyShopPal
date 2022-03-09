package com.example.myshoppal.ui.fragments.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FirestoreClass
import com.example.myshoppal.models.User
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.iv_user_photo
import java.io.IOException

class UserProfileActivity : BaseActivity() , View.OnClickListener{
    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri:Uri? = null

    private var mUserProfileImageURL: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)



        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUserDetails = intent.getParcelableExtra<User>(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.setText(mUserDetails.firstName)
        et_last_name.setText(mUserDetails.lastName)
        et_email.setText(mUserDetails.email)
        et_email.isEnabled =false


        if (mUserDetails.profileCompleted == 0){
            tv_title_user_profile.text = resources.getString(R.string.title_complete_profile)
            et_first_name.isEnabled = false

            et_last_name.isEnabled =false

            et_email.isEnabled =false


        }else{
            setupActionBar()
            tv_title_user_profile.text = resources.getString(R.string.title_edit_profile)
            GlideLoader(this).loadUserPicture(mUserDetails.image, iv_user_photo)

            if (mUserDetails.mobile != 0L){
                et_mobile_number.setText(mUserDetails.mobile.toString())
            }


            if (mUserDetails.gender == Constants.MALE){
                rb_male.isChecked = true
            }else{
                rb_female.isChecked = true
            }

        }
        btn_submit.setOnClickListener(this)

        iv_user_photo.setOnClickListener(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_user_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if(view != null) {
            when (view.id) {

                R.id.btn_submit ->{
                    if(validateUserProfileDetails()){
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null){
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.USER_PROFILE_IMAGE)
                        }else{
                            updateUserProfileDetails()
                        }

                    }

                }

                R.id.iv_user_photo -> {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)

                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails(){

            val userHashMap = HashMap<String, Any>()

            val firstName = et_first_name.text.toString().trim { it <= ' ' }
            if(firstName != mUserDetails.firstName){
                userHashMap[Constants.FIRST_NAME] = firstName
            }

          val lastName = et_last_name.text.toString().trim { it <= ' ' }
          if(lastName != mUserDetails.lastName){
            userHashMap[Constants.LAST_NAME] = lastName
          }


        val mobileNumber = et_mobile_number.text.toString().trim { it <= ' ' }
            if(mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()){
                userHashMap[Constants.MOBILE] = mobileNumber.toLong()
            }

        if(mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

            val gender = if(rb_male.isChecked){
                Constants.MALE
            }else{
                Constants.FEMALE
            }

        if(gender.isNotEmpty() && gender != mUserDetails.gender){
            userHashMap[Constants.GENDER] = gender
        }
            userHashMap[Constants.GENDER] = gender

           userHashMap[Constants.COMPLETE_PROFILE] = 1

            FirestoreClass().updateUserProfileData(this, userHashMap)

    }

    fun userUpdateProfileUpdateSuccess(){
        hideProgressDialog()

        Toast.makeText(this, resources.getString(R.string.msg_profile_update_success), Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                Toast.makeText(this, "Oops you just denied the permission for camera.You can allow it in settings.",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if(data != null){
                    try {
                        mSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_user_photo )
                    }catch (e:IOException){
                        e.printStackTrace()
                        Toast.makeText(this, resources.getString(R.string.image_selection_failed), Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Request Cancelled", "Image Selection Cancelled")
        }
    }

    private fun validateUserProfileDetails(): Boolean{
        return when{
            TextUtils.isEmpty(et_mobile_number.text.toString().trim{ it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageUrl: String){
        mUserProfileImageURL = imageUrl
        updateUserProfileDetails()
    }
}