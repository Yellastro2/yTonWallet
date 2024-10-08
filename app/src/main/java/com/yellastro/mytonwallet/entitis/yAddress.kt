package com.yellastro.mytonwallet.entitis

import com.yellastro.mytonwallet.baseEmoji

class yAddress(
    val address: String,
    val name: String? = null,
    val image: String? = null,
    var lastDate: Int = 0
){
    val title: String
        get() {
            return name?: addressShort
        }

    val addressShort: String
        get() {
            return address.substring(0,4) + "..." + address.substring(address.length-4,
                address.length)
        }

    val letter: String
        get() {
            return if (name.isNullOrEmpty()) baseEmoji else name.substring(0,1)
        }
}