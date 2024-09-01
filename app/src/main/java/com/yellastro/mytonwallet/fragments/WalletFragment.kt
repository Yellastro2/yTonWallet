package com.yellastro.mytonwallet.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.yellastro.mytonwallet.entitis.yJetton
import com.yellastro.mytonwallet.viewmodels.NewPincodeModel
import com.yellastro.mytonwallet.viewmodels.WalletModel


class WalletFragment : Fragment() {

    lateinit var navController: NavController

    val viewModel: WalletModel by viewModels()

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

        view.findViewById<NavigationBarView>(R.id.bottom_navigation).setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.item_2 -> {
                    // Respond to navigation item 2 click
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




        val recyclerView = view.findViewById<RecyclerView>(R.id.fr_wallet_jetton_list)
        recyclerView.adapter = viewModel.mJettonAdapter

        loadJettons()

    }

    val someTokensName = listOf("Staked TON","Tether USD", "USDT", "NOT")

    private fun loadJettons() {
        val fList = ArrayList<yJetton>()
        val fFrom = ArrayList<String>()
        fFrom.addAll(someTokensName)
        for (i in someTokensName.indices){
            fList.add(yJetton())
        }
    }

}