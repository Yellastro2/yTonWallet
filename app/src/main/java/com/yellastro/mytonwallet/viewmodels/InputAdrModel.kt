package com.yellastro.mytonwallet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yellastro.mytonwallet.adapters.ChoseCurAdapter
import com.yellastro.mytonwallet.adapters.ContactAdapter
import com.yellastro.mytonwallet.entitis.yJetton

class InputAdrModel(application: Application) : AndroidViewModel(application) {

    var mJettonAdapter = ContactAdapter()

    init {
        mJettonAdapter.setData(sAddressContact)
    }
}