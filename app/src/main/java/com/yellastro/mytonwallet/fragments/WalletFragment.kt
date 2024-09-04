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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationBarView
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.viewmodels.WalletModel
import com.yellastro.mytonwallet.views.yDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.round


class WalletFragment : Fragment() {

    lateinit var navController: NavController

    val IMG_HOLDER = "https://kartinki.pics/uploads/posts/2022-12/1670368675_24-kartinkin-net-p-nindzya-sobaka-vkontakte-24.png"

    val viewModel: WalletModel by viewModels()

    lateinit var mvBalance: TextView
    lateinit var mvBalanceColapced: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            loadAll()
        }
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
                    viewModel.loadHistory()
                    true
                }
                R.id.item_2 -> {
                    // Respond to navigation item 2 click
                    viewModel.loadJettons({fValue -> setBalance(fValue)})
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

        view.findViewById<View>(R.id.fr_wallet_btn_send).setOnClickListener {
            navController.navigate(R.id.action_walletFragment_to_transChoseCurFragment)
        }

        val fvSwipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.fr_wallet_refreshlay)
        fvSwipeRefresh.setOnRefreshListener {
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                loadAll()
                viewModel.viewModelScope.launch(Dispatchers.Main) {
                    fvSwipeRefresh.isRefreshing = false
                }
            }
        }



        val fvListJettons = view.findViewById<RecyclerView>(R.id.fr_wallet_jetton_list)
        fvListJettons.adapter = viewModel.mJettonAdapter
        fvListJettons.addItemDecoration(yDecorator.getDecorator(requireContext()))

        val fvListHistory = view.findViewById<RecyclerView>(R.id.fr_wallet_history_list)
        fvListHistory.adapter = viewModel.mHistoryAdapter
        fvListHistory.addItemDecoration(yDecorator.getDecorator(requireContext()))



//        EventInfoFragment().show(childFragmentManager,"")

        viewModel.mHistoryAdapter.setFragManager(childFragmentManager)

//        val navController = findNavController()
//        navController.navigate(R.id.action_walletFragment_to_eventInfoFragment)

    }

    fun loadAll(){
        viewModel.loadJettons({fValue -> setBalance(fValue)})
        viewModel.loadHistory()
    }



    fun setBalance(fBalance: Float){
        val fInt = (fBalance).toInt()
        val fValueStr = "%,d".format(fInt).replace(',',' ')

        var fDecimal = (fBalance - fInt)

        if (fBalance > 0.1){
            fDecimal = round(fDecimal * 100) / 100
        }

        val fBody = "$" + fValueStr + fDecimal.toString().removePrefix("0")

//        val fBody = "$" + floatToPrint(fBalance).replace(","," ")

        mvBalance.text = fBody
        mvBalanceColapced.text = fBody
    }

}