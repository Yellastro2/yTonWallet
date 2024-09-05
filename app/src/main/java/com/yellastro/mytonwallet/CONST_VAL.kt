package com.yellastro.mytonwallet

import java.time.format.DateTimeFormatter
import java.util.Locale


val PREF_KEY = "pref_key"
val PIN = "pin"
val BIO_LOGIN = "biologin"
val MNEMO = "mnemo"

val hourFormater = DateTimeFormatter.ofPattern("HH:mm", Locale.US)
val fullFormater = DateTimeFormatter.ofPattern("dd MMM, HH:mm", Locale.US)
val mDayFormat = DateTimeFormatter.ofPattern("MMM dd", Locale.US)

val baseEmoji = "<3"

val HISTORY_SIZE = 8

class CONST_VAL {
}