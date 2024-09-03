package com.yellastro.mytonwallet.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.yellastro.mytonwallet.adapters.ChoseCurAdapter
import com.yellastro.mytonwallet.entitis.yJetton

class ChoseCurModel(application: Application) : AndroidViewModel(application) {

    var mJettonAdapter = ChoseCurAdapter()

    init {
        mJettonAdapter.setData(sJettonsWallet.filter { !it.isStaking } as ArrayList<yJetton>)
    }

}