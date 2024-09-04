package com.yellastro.mytonwallet.entitis

class yJetton(val title: String,
              val symbol: String,
              val name: String,
              val value: Double,
              val priceChange: Float = 0F,
              val usdPrice: Float,
              val image: String,
              val isStaking: Boolean = false,
              val APY: Float = 0F,
              ) {

    val valueUsd: Double
    init {
        valueUsd = value * usdPrice
    }

}