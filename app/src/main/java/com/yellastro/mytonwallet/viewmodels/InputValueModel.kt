package com.yellastro.mytonwallet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.yellastro.mytonwallet.adapters.ChoseCurAdapter
import com.yellastro.mytonwallet.adapters.ContactAdapter
import com.yellastro.mytonwallet.entitis.yJetton

class InputValueModel : ViewModel() {

    var mJetton: yJetton? = null
    var mValue: Double = 0.0
    var isUsdValue = false

}