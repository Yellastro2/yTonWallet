package com.yellastro.mytonwallet.pro_animations

import android.view.ViewGroup
import android.widget.TextView

fun onOffsetCollapseTitle(fvExpand: TextView, fvColps: TextView, progress: Float, fromSp: Float){
    val initialX = fvExpand.left
    val targetX = fvColps.left
    val initialY = fvExpand.top
    val targetY = fvColps.top

    val newX = initialX + (targetX - initialX) * progress
    val newY = initialY + (targetY - initialY) * progress
    val startTextSize = fromSp * fvExpand.getResources().getDisplayMetrics().scaledDensity // sp

    // Конечные параметры
    val endTextSize = fvColps.textSize.toFloat()  // sp

    val fProgresTextSize = startTextSize * (1F - progress) + endTextSize * progress

//        fvExpand.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, fProgresTextSize)
    fvExpand.setTextSize(fProgresTextSize / fvExpand.getResources().getDisplayMetrics().scaledDensity)

    fvExpand.translationX = newX - fvExpand.left
    fvExpand.translationY = newY - fvExpand.top
}

fun onOffsetCollapseText(fvTextView: TextView, progress: Float, baseTextSize: Int){


    val startTextSize = baseTextSize

    val endTextSize = 0  // sp

    val fProgresTextSize = startTextSize * (1F - progress) + endTextSize * progress

    fvTextView.setTextSize(fProgresTextSize / fvTextView.getResources().getDisplayMetrics().scaledDensity)


}

fun onOffsetCollapseTextIcon(fvExpandLay: ViewGroup, progress: Float, baseSizeImg: Int, baseTextSize: Int){
    val fvExpandImg = fvExpandLay.getChildAt(0)
    val fvExpText = fvExpandLay.getChildAt(1) as TextView
    val initialImgSize = baseSizeImg
    val targetImgSize = 0

    val newSizeImg = initialImgSize + (targetImgSize - initialImgSize) * progress
    val startTextSize = baseTextSize

    // Конечные параметры
    val endTextSize = 0  // sp

    val fProgresTextSize = startTextSize * (1F - progress) + endTextSize * progress

    fvExpText.setTextSize(fProgresTextSize / fvExpandLay.getResources().getDisplayMetrics().scaledDensity)

    fvExpandImg.layoutParams.width = newSizeImg.toInt()
    fvExpandImg.layoutParams.height = newSizeImg.toInt()
//    fvExpandImg.requestLayout()

}

class CollapsedText {
}