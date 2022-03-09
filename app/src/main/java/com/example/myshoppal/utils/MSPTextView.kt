package com.example.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlinx.android.synthetic.main.activity_splash.*

class MSPTextView(context: Context, attrs:AttributeSet): AppCompatTextView(context, attrs) {

    init {
        applyFont()
    }

    private fun applyFont(){
        val typeFace: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = typeFace
    }
}