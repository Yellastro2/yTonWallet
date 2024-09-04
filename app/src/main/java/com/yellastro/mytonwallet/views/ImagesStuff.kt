package com.yellastro.mytonwallet.views

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
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

val gradientCache = HashMap<Int,GradientDrawable>()


fun setTransAva(fAdr: yAddress, viewHolder: EntityHolder) {
    viewHolder.mvIconSingleLay.visibility = View.VISIBLE
    viewHolder.mvIconSwapLay.visibility = View.GONE
    setTransAvaToViews(fAdr, viewHolder.mvIcon, viewHolder.mvIconSymbol)
}

fun setTransAvaToViews(fAddress: yAddress, fvIcon: ImageView, fvSymbol: TextView){
    val fStart = System.currentTimeMillis()
    if (fAddress.image.isNullOrEmpty()){
        GlobalScope.launch(Dispatchers.IO) {
            val fid = Random.nextInt(gradientStore.size)
            var gd: GradientDrawable?
            if (fid in gradientCache)
                gd = gradientCache[fid]
            else {
                val fGrad = gradientStore.toList()[fid]


                gd = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                        Color.parseColor(fGrad.first),
                        Color.parseColor(fGrad.second)
                    )
                )
                gd.cornerRadius = 0f
                gradientCache.put(fid,gd)
            }

            GlobalScope.launch(Dispatchers.Main){
                val fStart2 = System.currentTimeMillis()
                fvIcon.setImageDrawable(gd)
                Log.i("speed","setTransAvaToViews - Gradient: ${System.currentTimeMillis() - fStart2}ms")
                Log.i("speed","setTransAvaToViews total - Gradient: ${System.currentTimeMillis() - fStart}ms")
            }
        }

        Log.i("speed","setTransAvaToViews end: ${System.currentTimeMillis() - fStart}ms")
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