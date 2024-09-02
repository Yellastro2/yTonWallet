package com.yellastro.mytonwallet.entitis

class yJetton(val title: String,
              val symbol: String,
              val value: Float,
              val priceChange: Float = 0F,
              val usdPrice: Float,
              val image: String,
              val isStaking: Boolean = false,
              val APY: Float = 0F,
              ) {

    val valueUsd: Float
    init {
        valueUsd = value * usdPrice
    }

}