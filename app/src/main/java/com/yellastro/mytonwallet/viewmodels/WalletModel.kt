package com.yellastro.mytonwallet.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yellastro.mytonwallet.ASSETS
import com.yellastro.mytonwallet.HISTORY_SIZE
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.adapters.HistoryAdapter
import com.yellastro.mytonwallet.adapters.JettonAdapter
import com.yellastro.mytonwallet.entitis.yAddress
import com.yellastro.mytonwallet.entitis.yEvent
import com.yellastro.mytonwallet.entitis.yJetton
import com.yellastro.mytonwallet.mDayFormat
import com.yellastro.mytonwallet.nftStore
import com.yellastro.mytonwallet.sAddressContact
import com.yellastro.mytonwallet.sHistoryData
import com.yellastro.mytonwallet.sJettonsWallet
import com.yellastro.mytonwallet.someAddress
import com.yellastro.mytonwallet.someNFTColl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import kotlin.math.round
import kotlin.random.Random




class WalletModel(application: Application) : AndroidViewModel(application) {
    var mHistoryAdapter = HistoryAdapter()
    var mJettonAdapter = JettonAdapter()



    init {
        mJettonAdapter.setData(sJettonsWallet)
        setHistoryToList(sHistoryData)
    }

    fun clearWallet(){
        sHistoryData = ArrayList()
        sJettonsWallet = ArrayList()
        mHistoryAdapter.setData(ArrayList())

        mJettonAdapter.setData(ArrayList())
    }

    fun setHistoryToList(fEvents: ArrayList<yEvent>){
//        fEvents.sortBy { -it.dateTime }
//
//        sHistoryData = fEvents

        var fToday = getApplication<Application>().resources.getString(R.string.wrd_today)



        for (i in 0..<fEvents.size) {

            val qDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(fEvents[i].dateTime * 1000L),
                TimeZone.getDefault().toZoneId()
            )
//            val qDate = Date(fEvents[i].dateTime * 1000L)
            if (i == 0) {
                LocalDateTime.now()
                if (LocalDateTime.now().dayOfYear != qDate.dayOfYear ||
                    LocalDateTime.now().year != qDate.year
                ) {
                    fToday = mDayFormat.format(qDate)
                }
                viewModelScope.launch(Dispatchers.Main) {
                    mHistoryAdapter.addItem(HistoryAdapter.yDateHistory(fToday))
                    mHistoryAdapter.addItem(fEvents[i])
                }
                continue
            }
            val qPrevDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(fEvents[i - 1].dateTime * 1000L),
                TimeZone.getDefault().toZoneId()
            )

            viewModelScope.launch(Dispatchers.Main) {
                if (qDate.dayOfYear < qPrevDate.dayOfYear ||
                    qDate.year < qPrevDate.year
                )
                    mHistoryAdapter.addItem(
                        HistoryAdapter.yDateHistory(
                            mDayFormat.format(
                                qDate
                            )
                        )
                    )

                mHistoryAdapter.addItem(fEvents[i])
            }
        }
    }

    fun loadHistory() {
        val fStart = System.currentTimeMillis()
        var fSpeedTotalAdditem = 0L

        mHistoryAdapter.dataSet = ArrayList()
        viewModelScope.launch(Dispatchers.Main) {
            mHistoryAdapter.notifyDataSetChanged() }

        val fList = ArrayList<yEvent>()
        val fPeriod = 500000
        val fNow = round((System.currentTimeMillis() / 1000).toDouble()).toInt()
        for (i in 0..HISTORY_SIZE) {
            val j = 1
            val qType = Random.nextInt(5)
            var qEvent: yEvent? = null
            if (qType < 3)
                qEvent =  yEvent(
                        yEvent.TRANS,
                        Random.nextBoolean(),
                        someAddress[Random.nextInt(someAddress.size)],
                        dateTime = fNow - Random.nextInt(fPeriod),
                        value = Random.nextInt(50, 5000).toDouble(),
                        symbol = someTokenssymb[j],
                        walletName = if (Random.nextInt(3)>1) "some.t.me" else null,
                        message = if (Random.nextBoolean()) "\uD83E\uDD73 Happy Birthday! Thank you for being such an amazing friend. I cherish every moment we spend together \uD83E\uDDE1"
                            else null,
                        isEncrypt = Random.nextBoolean()
                    )

            else if (qType == 3){
                var k = j
                do {
                    k = Random.nextInt(someTokensName.size)
                }while (k == j)

                qEvent = yEvent(
                        yEvent.SWAP,
                        false, // TODO dosent matter
                        "", // TODO dosent matter
                        dateTime = fNow - Random.nextInt(fPeriod),
                        value = Random.nextInt(50, 5000).toDouble(),
                        symbol = someTokenssymb[j],
                        symbolSwap = someTokenssymb[k],
                        valueSwap = Random.nextInt(50, 5000).toDouble(),
                    )

            } else {
                val fCol = someNFTColl[Random.nextInt(someNFTColl.size)]
                val fItem = nftStore[fCol]!!.get(Random.nextInt(nftStore[fCol]!!.size))

                qEvent = yEvent(
                        yEvent.NFT,
                        Random.nextBoolean(),
                        someAddress[Random.nextInt(someAddress.size)],
                        dateTime = fNow - Random.nextInt(fPeriod),
                        value = Random.nextInt(50, 5000).toDouble(),
                        symbol = fCol,
                        nftName = fItem[0],
                        nftImageLink = fItem[1]
                    )
            }

            if (qEvent.type in listOf(yEvent.NFT,yEvent.TRANS)){
                var qAddress: yAddress? = null
                if (!sAddressContact.any {
                            qCont: yAddress ->
                        if((!qCont.name.isNullOrEmpty() && qCont.name == qEvent.walletName)
                            || qCont.address == qEvent.addressFrom)
                        {
                            qAddress = qCont
                            true
                        } else false
                    }) {
                    qAddress = yAddress(
                        qEvent.addressFrom,
                        qEvent.walletName,
                        qEvent.imageAva,
                        qEvent.dateTime
                    )
                    sAddressContact.add(qAddress!!)
                }
                qEvent.addressEntity = qAddress
            }
            fList.add(qEvent)
        }

        setHistoryToList(fList)

        Log.i("speed","WalletModel.loadHistory total ms: ${System.currentTimeMillis() - fStart}")
    }

    val someTokensName = listOf("Staked TON","Tether USD", "Toncoin", "NOT", "\$MY")

    val someTokenssymb = listOf("TON","USD₮", "TON", "NOT", "MY")


}