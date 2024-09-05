package com.yellastro.mytonwallet

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yellastro.mytonwallet.entitis.yAddress
import com.yellastro.mytonwallet.entitis.yEvent
import com.yellastro.mytonwallet.entitis.yJetton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.random.Random



fun loadJettons() {
    val fList = ArrayList<yJetton>()
    val fFrom = ArrayList<String>()
    fFrom.addAll(ASSETS.keys)

    for (i in 0..<Random.nextInt(2,ASSETS.size)) {
        val j = Random.nextInt(fFrom.size)
        val qValue = Random.nextInt(50, 5000).toDouble()
        val qUsdRate = Random.nextDouble() * 100



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
//    mJettonAdapter.setData(fList)
    sJettonsWallet = fList
}

fun loadHistory() {
    val fStart = System.currentTimeMillis()


    val fList = ArrayList<yEvent>()
    val fPeriod = 500000
    val fNow = round((System.currentTimeMillis() / 1000).toDouble()).toInt()
    for (i in 0..HISTORY_SIZE) {
        val j = Random.nextInt(ASSETS.size)
        val qType = Random.nextInt(5)
        var qEvent: yEvent? = null
        if (qType < 3)
            qEvent =  yEvent(
                yEvent.TRANS,
                Random.nextBoolean(),
                someAddress[Random.nextInt(someAddress.size)],
                dateTime = fNow - Random.nextInt(fPeriod),
                value = Random.nextInt(50, 5000).toDouble(),
                symbol = ASSETS.keys.toList()[j],
                walletName = if (Random.nextInt(3)>1) "some.t.me" else null,
                message = if (Random.nextBoolean()) "❤\uFE0F Happy Birthday! Thank you for being such an amazing friend. I cherish every moment we spend together \uD83E\uDDE1"
                else null,
                isEncrypt = Random.nextBoolean()
            )

        else if (qType == 3){
            var k = j
            do {
                k = Random.nextInt(ASSETS.size)
            }while (k == j)

            qEvent = yEvent(
                yEvent.SWAP,
                false, // TODO dosent matter
                "", // TODO dosent matter
                dateTime = fNow - Random.nextInt(fPeriod),
                value = Random.nextInt(50, 5000).toDouble(),
                symbol = ASSETS.keys.toList()[j],
                symbolSwap = ASSETS.keys.toList()[k],
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

        if (qEvent.type in listOf(yEvent.NFT, yEvent.TRANS)){
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

    sHistoryData = fList
//    setHistoryToList(fList)

    Log.i("speed","WalletModel.loadHistory total ms: ${System.currentTimeMillis() - fStart}")
}