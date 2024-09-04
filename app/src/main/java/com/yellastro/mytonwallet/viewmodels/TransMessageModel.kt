package com.yellastro.mytonwallet.viewmodels

import androidx.lifecycle.ViewModel
import com.yellastro.mytonwallet.entitis.yAddress

class TransMessageModel: ViewModel() {
    var mAddress: String = ""

    var mJetton: String = ""
    var mAmount: Double  = 0.0

    val mContact: yAddress?
        get() {
            return sAddressContact.find { it.address == mAddress }
        }
}