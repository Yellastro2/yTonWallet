package com.yellastro.mytonwallet.views

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.adapters.EntityHolder
import com.yellastro.mytonwallet.entitis.yAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random


val gradientStore = mapOf("#E0A2F3" to "#D669ED","#FF885E" to "#FF516A", "#98D163" to "#48BA44",
    "#B493F8" to "#6E62E0", "#5ACAE3" to "#369BD4", "#FE89AB" to "#DA5675", "#FEBA5A" to "#F68237",
    "#5BAEF9" to "#418BD0")



fun setTransAva(fAdr: yAddress, viewHolder: EntityHolder) {
    viewHolder.mvIconSingleLay.visibility = View.VISIBLE
    viewHolder.mvIconSwapLay.visibility = View.GONE
    setTransAvaToViews(fAdr, viewHolder.mvIcon, viewHolder.mvIconSymbol)
}

fun setTransAvaToViews(fAddress: yAddress, fvIcon: ImageView, fvSymbol: TextView){
    if (fAddress.image.isNullOrEmpty()){
        val fGrad = gradientStore.toList()[Random.nextInt(gradientStore.size)]

        GlobalScope.launch(Dispatchers.IO) {
            val gd = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    Color.parseColor(fGrad.first),
                    Color.parseColor(fGrad.second))
            )
            gd.cornerRadius = 0f

            GlobalScope.launch(Dispatchers.Main){fvIcon.setImageDrawable(gd)}
        }


        fvSymbol.visibility = View.VISIBLE
        fvSymbol.text = fAddress.letter

    }else{
        fvSymbol.visibility = View.GONE
        fvIcon.load(fAddress.image){
            crossfade(true)
            placeholder(R.drawable.img_jet_holder)
        }
    }
}

class ImagesStuff {
}