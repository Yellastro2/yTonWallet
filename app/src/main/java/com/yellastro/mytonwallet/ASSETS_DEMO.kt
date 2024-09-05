package com.yellastro.mytonwallet

import com.yellastro.mytonwallet.adapters.HistoryAdapter
import com.yellastro.mytonwallet.entitis.yAddress
import com.yellastro.mytonwallet.entitis.yEvent
import com.yellastro.mytonwallet.entitis.yJetton
import java.time.format.DateTimeFormatter
import java.util.Locale


val usdRates = mapOf<String,Float>("TON" to 8F,"Staked TON" to 8F,"NOT" to 0.01F, "MY" to 0.06F)

val someAddress = listOf("EQARbK3z2a2cYE1gXVNKk3O5OgpaxfoyD1VfQ7aNtpd2qcYV",
    "EQCuy17OHOlMVRz0VGGwwBa1_pQfFlWhk6SaiK1egCiF5Cwp",
    "EQAWH3Rqwh5jYEPvQplUbWyyWakENSVkCmhdjCntvDdMs_yx",
    "EQA6FPupjsjSsTIWrP_j8l0kbVCfl-bAYWhfkBDdyFdWPaC0")





val ASSETS = mapOf("Staked TON" to listOf("sTON","Staked TON","https://ton.org/download/ton_symbol.png"),
    "USD₮" to listOf("USD₮", "Tether USD", "https://cache.tonapi.io/imgproxy/T3PB4s7oprNVaJkwqbGg54nexKE0zzKhcrPv8jcWYzU/rs:fill:200:200:1/g:no/aHR0cHM6Ly90ZXRoZXIudG8vaW1hZ2VzL2xvZ29DaXJjbGUucG5n.webp"),
    "TON" to listOf("TON", "Toncoin", "https://ton.org/download/ton_symbol.png"),
    "NOT" to listOf("NOT", "Notcoin", "https://cache.tonapi.io/imgproxy/4KCMNm34jZLXt0rqeFm4rH-BK4FoK76EVX9r0cCIGDg/rs:fill:200:200:1/g:no/aHR0cHM6Ly9jZG4uam9pbmNvbW11bml0eS54eXovY2xpY2tlci9ub3RfbG9nby5wbmc.webp"),
    "MY" to listOf("MY", "MyTonWallet Coin", "https://cache.tonapi.io/imgproxy/Qy038wCBKISofJ0hYMlj6COWma330cx3Ju1ZSPM2LRU/rs:fill:200:200:1/g:no/aHR0cHM6Ly9teXRvbndhbGxldC5pby9sb2dvLTI1Ni1ibHVlLnBuZw.webp"),
)


val someNFTColl = listOf("Rich Cats","Telegram Usernames")

val nftStore = mapOf("Rich Cats" to listOf(
    listOf("Cat #1629",
        "https://s.getgems.io/nft/c/64579892411ec1efb1dcba31/0/image.png")
),
    "Telegram Usernames" to listOf(
        listOf("@lame","https://nft.fragment.com/username/lame.webp"),
        listOf("@vamp","https://nft.fragment.com/username/vamp.webp")))


// kwonn its cringe practice to set any dynamic data in static values, but its only demo =_=
var sJettonsWallet = ArrayList<yJetton>()
var sHistoryData = ArrayList<yEvent>()



var sAddressContact = ArrayList<yAddress>()

class ASSETS_DEMO {
}