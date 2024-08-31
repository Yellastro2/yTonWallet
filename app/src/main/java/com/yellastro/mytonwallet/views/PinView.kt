package com.yellastro.mytonwallet.views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.yellastro.mytonwallet.R

class PinView(
    val context: Context,
    parent: ViewGroup,
    val pinsize: Int,
    val dotEmpty: Int = R.drawable.bkg_pin_empty,
    val dotFill: Int = R.drawable.bkg_pin_fill
) {

    var mvPinDots: ViewGroup = LayoutInflater.from(context).inflate(R.layout.pin_view, parent) as ViewGroup


    init{
        mvPinDots = parent.getChildAt(0) as ViewGroup
        for (i in 0..<pinsize){
            LayoutInflater.from(context).inflate(R.layout.pindot_view, mvPinDots)
        }

    }

    fun setDots(fSize: Int){

        for (i in 0..<pinsize){
            val qvDot = (mvPinDots.getChildAt(i) as ImageView)

            if (i < fSize){
                qvDot.setImageResource(dotFill)
            }else{
                qvDot.setImageResource(dotEmpty)
            }

        }
    }

    fun setColor(fColor: Int){
        for (i in 0..<pinsize) {
            val qvDot = (mvPinDots.getChildAt(i) as ImageView)
            val qDrawable = qvDot.drawable
            qDrawable.setTint(fColor)
            qvDot.setImageDrawable(qDrawable)
        }
    }

    fun clear(){
        for (i in 0..<pinsize) {
            val qvDot = (mvPinDots.getChildAt(i) as ImageView)
            qvDot.drawable.setTintList(null)
        }
    }
}