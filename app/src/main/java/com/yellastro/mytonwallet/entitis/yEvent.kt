package com.yellastro.mytonwallet.entitis

import com.yellastro.mytonwallet.adapters.yHistoryEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

val hourFormater = DateTimeFormatter.ofPattern("HH:mm")

class yEvent(
    val type: String,
    val isSend: Boolean,
    val addressFrom: String,
    val walletName: String? = null,
    val dateTime: Int,
    val value: Float,
    val symbol: String,
    val image: String? = null,

) : yHistoryEntity {
    val letter: String
        get() {
            return if (walletName.isNullOrEmpty()) "􀾟" else walletName.substring(0,1)
        }
    val time: String
        get() {
             return hourFormater.format(
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dateTime * 1000L),
                        TimeZone.getDefault().toZoneId()))
        }

    val title: String
        get() {
            return walletName?:
                (addressFrom.substring(0,4) + "..." + addressFrom.substring(addressFrom.length-4,
                                                                                   addressFrom.length))
        }

    companion object {
        val TRANS = "trans"
    }
}