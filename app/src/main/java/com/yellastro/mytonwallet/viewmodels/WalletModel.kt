package com.yellastro.mytonwallet.viewmodels

import androidx.lifecycle.ViewModel
import com.yellastro.mytonwallet.adapters.HistoryAdapter
import com.yellastro.mytonwallet.adapters.JettonAdapter

class WalletModel: ViewModel() {
    val mHistoryAdapter = HistoryAdapter()
    var mJettonAdapter = JettonAdapter()
}