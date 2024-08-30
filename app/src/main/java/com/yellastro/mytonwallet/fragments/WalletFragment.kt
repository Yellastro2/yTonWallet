package com.yellastro.mytonwallet.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationBarView
import com.yellastro.mytonwallet.MNEMO
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R

class WalletFragment : Fragment() {

    lateinit var navController: NavController

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

        navController = findNavController()

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

    }

}