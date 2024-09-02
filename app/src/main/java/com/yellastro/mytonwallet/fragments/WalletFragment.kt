package com.yellastro.mytonwallet.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.navigation.NavigationBarView
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.adapters.JettonAdapter
import com.yellastro.mytonwallet.adapters.yDateHistory
import com.yellastro.mytonwallet.adapters.yHistoryEntity
import com.yellastro.mytonwallet.entitis.yEvent
import com.yellastro.mytonwallet.entitis.yJetton
import com.yellastro.mytonwallet.viewmodels.NewPincodeModel
import com.yellastro.mytonwallet.viewmodels.WalletModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.round
import kotlin.random.Random


class WalletFragment : Fragment() {

    lateinit var navController: NavController

    val viewModel: WalletModel by viewModels()

    lateinit var mvBalance: TextView
    lateinit var mvBalanceColapced: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        navController = findNavController()

        val fvCollapseView = view.findViewById<View>(R.id.fr_wallet_toolbar_colaps_lay)

        mvBalance = view.findViewById(R.id.fr_wallet_balance_text)
        mvBalanceColapced = view.findViewById(R.id.fr_wallet_balance_colapse)

        view.findViewById<NavigationBarView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    loadHistory()
                    true
                }
                R.id.item_2 -> {
                    // Respond to navigation item 2 click
                    loadJettons()
                    true
                }
                R.id.item_3 -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.item_4 -> {
                    requireActivity()
                        .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                        .edit().clear().apply()
                    navController.popBackStack(R.id.walletFragment,true)
                    navController.navigate(R.id.welcomeFrag)
                    true
                }
                else -> false
            }
        }

        view.findViewById<AppBarLayout>(R.id.app_bar)
            .addOnOffsetChangedListener { appBarLayout, verticalOffset ->

                val some1 = toolbar.getHeight() + verticalOffset
                val some2 =  2 * ViewCompat.getMinimumHeight(toolbar)
                Log.i("colapse","toolheight= ${toolbar.getHeight()}, offset = ${verticalOffset}, some2 = ${some2}")
            if ( verticalOffset * -1 > toolbar.getHeight()) {
                fvCollapseView.animate().alpha(1F).setDuration(100)
            } else {
                fvCollapseView.animate().alpha(0F).setDuration(100)
            }
        }




        val recyclerViewJet = view.findViewById<RecyclerView>(R.id.fr_wallet_jetton_list)
        recyclerViewJet.adapter = viewModel.mJettonAdapter

        val recyclerViewHist = view.findViewById<RecyclerView>(R.id.fr_wallet_history_list)
        recyclerViewHist.adapter = viewModel.mHistoryAdapter

        loadJettons()
        loadHistory()

    }

    val someAddress = listOf("EQARbK3z2a2cYE1gXVNKk3O5OgpaxfoyD1VfQ7aNtpd2qcYV",
        "EQCuy17OHOlMVRz0VGGwwBa1_pQfFlWhk6SaiK1egCiF5Cwp",
        "EQAWH3Rqwh5jYEPvQplUbWyyWakENSVkCmhdjCntvDdMs_yx",
        "EQA6FPupjsjSsTIWrP_j8l0kbVCfl-bAYWhfkBDdyFdWPaC0")

    private fun loadHistory() {
        val fList = ArrayList<yEvent>()
        val fPeriod = 500000
        val fNow = round((System.currentTimeMillis() / 1000).toDouble()).toInt()
        for (i in 0..50){
            val j = Random.nextInt(someTokensName.size)
            fList.add(
                yEvent(
                    yEvent.TRANS,
                    Random.nextBoolean(),
                    someAddress[Random.nextInt(someAddress.size)],
                    dateTime = fNow - Random.nextInt(fPeriod),
                    value = Random.nextInt(50,5000).toFloat(),
                    symbol = someTokenssymb[j]
                )
            )
        }

        fList.sortBy { -it.dateTime }
        val fDatedList = ArrayList<yHistoryEntity>()
        var fToday = resources.getString(R.string.wrd_today)

        val fDayFormat = DateTimeFormatter.ofPattern("MMM dd", Locale.US)

        for (i in 0..<fList.size){

            val qDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(fList[i].dateTime * 1000L),
                TimeZone.getDefault().toZoneId())
//            val qDate = Date(fList[i].dateTime * 1000L)
            if (i == 0) {
                LocalDateTime.now()
                if (LocalDateTime.now().dayOfYear != qDate.dayOfYear ||
                    LocalDateTime.now().year != qDate.year){
                    fToday = fDayFormat.format(qDate)
                }
                fDatedList.add(yDateHistory(fToday))
                fDatedList.add(fList[i])
                continue
            }
            val qPrevDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(fList[i-1].dateTime * 1000L),
                TimeZone.getDefault().toZoneId())

            if (qDate.dayOfYear < qPrevDate.dayOfYear ||
                qDate.year < qPrevDate.year)
                fDatedList.add(yDateHistory(fDayFormat.format(qDate)))
            fDatedList.add(fList[i])
        }

        viewModel.mHistoryAdapter.setData(fDatedList)
    }

    val someTokensName = listOf("Staked TON","Tether USD", "Toncoin", "NOT")

    val someTokenssymb = listOf("TON","USD₮", "TON", "NOT")

    val someImages = listOf("https://ton.org/download/ton_symbol.png",
        "https://cache.tonapi.io/imgproxy/T3PB4s7oprNVaJkwqbGg54nexKE0zzKhcrPv8jcWYzU/rs:fill:200:200:1/g:no/aHR0cHM6Ly90ZXRoZXIudG8vaW1hZ2VzL2xvZ29DaXJjbGUucG5n.webp",
        "https://ton.org/download/ton_symbol.png",
        "https://cache.tonapi.io/imgproxy/4KCMNm34jZLXt0rqeFm4rH-BK4FoK76EVX9r0cCIGDg/rs:fill:200:200:1/g:no/aHR0cHM6Ly9jZG4uam9pbmNvbW11bml0eS54eXovY2xpY2tlci9ub3RfbG9nby5wbmc.webp")

    private fun loadJettons() {
        val fList = ArrayList<yJetton>()
        val fFrom = ArrayList<String>()
        val fImage = ArrayList<String>()
        fImage.addAll(someImages)
        fFrom.addAll(someTokensName)
        val fSymb = ArrayList<String>()
        fSymb.addAll(someTokenssymb)

        var fTotalUsd = 0F

        for (i in someTokensName.indices){
            val j = Random.nextInt(fFrom.size)
            val qValue = Random.nextInt(50,5000).toFloat()
            val qUsdRate = Random.nextFloat() * 100

            fTotalUsd += qValue * qUsdRate

            fList.add(yJetton(fFrom[j],
                fSymb[j],
                qValue,
                if (fSymb[j] == "USD₮" || fFrom[j] == "Staked TON") 0F else Random.nextFloat()*10 - 5F,
                qUsdRate,
                fImage[j],
                Random.nextBoolean(),
                Random.nextInt(5,20).toFloat()
                ))
            fImage.removeAt(j)
            fFrom.removeAt(j)
            fSymb.removeAt(j)
        }

        setBalance(fTotalUsd)

        viewModel.mJettonAdapter.setData(fList)
    }

    fun setBalance(fBalance: Float){
        val fInt = (fBalance).toInt()
        val fValueStr = "%,d".format(fInt).replace(',',' ')

        var fDecimal = (fBalance - fInt)

        if (fBalance > 0.1){
            fDecimal = round(fDecimal * 100) / 100
        }

        val fBody = "$" + fValueStr + fDecimal.toString().removePrefix("0")
        mvBalance.text = fBody
        mvBalanceColapced.text = fBody
    }

}