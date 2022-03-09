package com.example.myshoppal.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "users"
    const val PRODUCTS:String = "products"
    const val MYSHOPPAL_PREFERENCES: String = "MyShopPalPrefs"
    const val SOLD_PRODUCTS = "sold_products"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val GENDER:String="gender"
    const val MOBILE:String = "mobile"

    const val MALE:String = "male"
    const val FEMALE:String = "female"

    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"
    const val PRODUCT_IMAGE = "Product_Image"

    const val EXTRA_PRODUCT_OWNER_ID = "extra_product_owner_id"

    const val USER_ID = "user_id"

    const val EXTRA_PRODUCT_ID = "extra_product_id"

    const val COMPLETE_PROFILE:String = "profileCompleted"

    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"

    const val DEFAULT_CART_QUANTITY:String = "1"

    const val CART_ITEMS = "cart_items"

    const val IMAGE = "image"

    const val PRODUCT_ID = "product_id"
    const val CART_QUANTITY = "cart_quantity"

    const val HOME = "home"
    const val OFFICE = "office"
    const val OTHER = "other"

    const val ADDRESSES = "addresses"

    const val EXTRA_ADDRESS_DETAIL = "AddressDetails"

    const val EXTRA_SELECT_ADDRESS = "extra_select_address"
    const val ADD_ADDRESS_REQUEST_CODE = 121

    const val EXTRA_SELECTED_ADDRESS = "extra_selected_address"

    const val ORDERS:String = "orders"

    const val STOCK_QUANTITY = "stock_quantity"

    const val EXTRA_MY_ORDER_DETAILS = "extra_MY_ORDER_DETAILS"

    const val EXTRA_SOLD_PRODUCT_DETAILS = "extra_sold_product_details"

    fun showImageChooser(activity:Activity){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri:Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }
}