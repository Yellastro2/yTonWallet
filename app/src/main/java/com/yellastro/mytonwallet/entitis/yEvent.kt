package com.yellastro.mytonwallet.entitis

import com.yellastro.mytonwallet.adapters.yHistoryEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

val hourFormater = DateTimeFormatter.ofPattern("HH:mm")
val fullFormater = DateTimeFormatter.ofPattern("dd MMM, HH:mm")

val baseEmoji = "\udbc3\udf9f"
val asdsf = Character.toChars(0xdbc3)

class yEvent(
    val type: String,
    val isSend: Boolean,
    val addressFrom: String,
    val walletName: String? = null,
    val dateTime: Int,
    val value: Double,
    val symbol: String, // or NFT collection
    val imageAva: String? = null,
    val symbolSwap: String? = null,
    val valueSwap: Double? = null,
    val nftName: String? = null,
    val nftImageLink: String? = null,
    val message: String? = null,
    val isEncrypt: Boolean = false,
    var addressEntity: yAddress? = null
) : yHistoryEntity {

    val time: String
        get() {
             return hourFormater.format(
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dateTime * 1000L),
                        TimeZone.getDefault().toZoneId()))

        }

    val dateFull: String
        get() {
            return fullFormater.format(
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dateTime * 1000L),
                    TimeZone.getDefault().toZoneId()))
        }

    val title: String
        get() {
            if (type in listOf(TRANS, NFT))
                return addressEntity!!.title
            else if (type == SWAP)
                return symbol
            return "not implemented"
        }


    val fee: Long
        get() {return 250_000_000}

    val transLink: String
        get() {
            return "https://tonviewer.com/transaction/6ab5e9b46e0715b32c9fe42a25f316f0f7731862c0d63db5c2705fb1f4d7e4d4"
        }

    companion object {
        val TRANS = "trans"
        val SWAP = "swap"
        val NFT = "nft"
    }
}