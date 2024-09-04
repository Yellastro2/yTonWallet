package com.yellastro.mytonwallet

import android.util.Log
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.abs
import kotlin.math.round

fun floatToPrint(fValue: Double, fDivider: String = ","): String {

    val fStart = System.currentTimeMillis()

    var fValueStr = ""
    if (fValue % 1 == 0.0 || abs(fValue) > 500){
        fValueStr = "%,d".format(fValue.toInt())
    }
    else if (abs(fValue) > 0.001){
        fValueStr = (round(fValue * 1000 ) / 1000).toString()

    }
//    else if (abs(fValue) > 0.00001){
//        fValueStr = (round(fValue * 100000 ) / 100000).toString()
//    }
    else
    {
        var fBig = fValue.toBigDecimal()

        fBig = (fBig * BigDecimal(12)).round(MathContext(1))
        fValueStr = fBig.toString()
    }
    val fRet = fValueStr.replace(",",fDivider)
    Log.i("speed","floatToPrint total ms: ${System.currentTimeMillis() - fStart}")
    return fRet
}
class FormattingStuff {
}