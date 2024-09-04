package com.yellastro.mytonwallet.viewmodels

import android.app.Application
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
import com.yellastro.mytonwallet.sJettonsWallet
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
    val mHistoryAdapter = HistoryAdapter()
    var mJettonAdapter = JettonAdapter()

    val someAddress = listOf("EQARbK3z2a2cYE1gXVNKk3O5OgpaxfoyD1VfQ7aNtpd2qcYV",
        "EQCuy17OHOlMVRz0VGGwwBa1_pQfFlWhk6SaiK1egCiF5Cwp",
        "EQAWH3Rqwh5jYEPvQplUbWyyWakENSVkCmhdjCntvDdMs_yx",
        "EQA6FPupjsjSsTIWrP_j8l0kbVCfl-bAYWhfkBDdyFdWPaC0")

    fun loadHistory() {
        viewModelScope.launch(Dispatchers.Main) {
            mHistoryAdapter.dataSet = ArrayList()
            mHistoryAdapter.notifyDataSetChanged()
        }

                val fList = ArrayList<yEvent>()
                val fPeriod = 500000
                val fNow = round((System.currentTimeMillis() / 1000).toDouble()).toInt()
                for (i in 0..HISTORY_SIZE) {
                    val j = Random.nextInt(someTokensName.size)
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

                fList.sortBy { -it.dateTime }

                var fToday = getApplication<Application>().resources.getString(R.string.wrd_today)

                for (i in 0..<fList.size) {

                    val qDate = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(fList[i].dateTime * 1000L),
                        TimeZone.getDefault().toZoneId()
                    )
//            val qDate = Date(fList[i].dateTime * 1000L)
                    if (i == 0) {
                        LocalDateTime.now()
                        if (LocalDateTime.now().dayOfYear != qDate.dayOfYear ||
                            LocalDateTime.now().year != qDate.year
                        ) {
                            fToday = mDayFormat.format(qDate)
                        }
                        viewModelScope.launch(Dispatchers.Main) {
                            mHistoryAdapter.addItem(HistoryAdapter.yDateHistory(fToday))
                            mHistoryAdapter.addItem(fList[i])
                        }
//                        fDatedList.add(yDateHistor(fToday))
//                        fDatedList.add(fList[i])y
                        continue
                    }
                    val qPrevDate = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(fList[i - 1].dateTime * 1000L),
                        TimeZone.getDefault().toZoneId()
                    )

                    if (qDate.dayOfYear < qPrevDate.dayOfYear ||
                        qDate.year < qPrevDate.year
                    )
                        viewModelScope.launch(Dispatchers.Main) {
                            mHistoryAdapter.addItem(
                                HistoryAdapter.yDateHistory(
                                    mDayFormat.format(
                                        qDate
                                    )
                                )
                            )
                        }
//                        fDatedList.add(yDateHistory(fDayFormat.format(qDate)))
//                    fDatedList.add(fList[i])
                    viewModelScope.launch(Dispatchers.Main) {
                        mHistoryAdapter.addItem(fList[i])
                    }
                }
//                viewModelScope.launch(Dispatchers.Main){
//                    mHistoryAdapter.setData(fDatedList)
//                }


    }

    val someTokensName = listOf("Staked TON","Tether USD", "Toncoin", "NOT", "\$MY")

    val someTokenssymb = listOf("TON","USD₮", "TON", "NOT", "MY")


    val someImages = listOf("https://ton.org/download/ton_symbol.png",
        "https://cache.tonapi.io/imgproxy/T3PB4s7oprNVaJkwqbGg54nexKE0zzKhcrPv8jcWYzU/rs:fill:200:200:1/g:no/aHR0cHM6Ly90ZXRoZXIudG8vaW1hZ2VzL2xvZ29DaXJjbGUucG5n.webp",
        "https://ton.org/download/ton_symbol.png",
        "https://cache.tonapi.io/imgproxy/4KCMNm34jZLXt0rqeFm4rH-BK4FoK76EVX9r0cCIGDg/rs:fill:200:200:1/g:no/aHR0cHM6Ly9jZG4uam9pbmNvbW11bml0eS54eXovY2xpY2tlci9ub3RfbG9nby5wbmc.webp",
        "https://cache.tonapi.io/imgproxy/Qy038wCBKISofJ0hYMlj6COWma330cx3Ju1ZSPM2LRU/rs:fill:200:200:1/g:no/aHR0cHM6Ly9teXRvbndhbGxldC5pby9sb2dvLTI1Ni1ibHVlLnBuZw.webp")

    fun loadJettons(setBalance: (Float)->Unit) {
        viewModelScope.
        launch(Dispatchers.IO) {
            val fList = ArrayList<yJetton>()
            val fFrom = ArrayList<String>()
            fFrom.addAll(ASSETS.keys)

            var fTotalUsd = 0.0

            for (i in 0..<Random.nextInt(2,someTokensName.size)) {
                val j = Random.nextInt(fFrom.size)
                val qValue = Random.nextInt(50, 5000).toDouble()
                val qUsdRate = Random.nextDouble() * 100

                fTotalUsd += qValue * qUsdRate

                val qAsset = ASSETS[fFrom[j]]!!
                val qSymb = qAsset[0]

                fList.add(
                    yJetton(
                        fFrom[j],
                        qSymb,
                        qAsset[1],
                        qValue,
                        if (qSymb == "USD₮" || qAsset[1] == "Staked TON") 0F else Random.nextFloat() * 10 - 5F,
                        qUsdRate.toFloat(),
                        qAsset[2],
                        Random.nextInt(4) == 0,
                        Random.nextInt(5, 20).toFloat()
                    )
                )
                fFrom.removeAt(j)
            }
            viewModelScope.launch(Dispatchers.Main) {
                setBalance(fTotalUsd.toFloat())
                mJettonAdapter.setData(fList)
                sJettonsWallet = fList
            }
        }

    }
}