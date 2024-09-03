package com.yellastro.mytonwallet.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yellastro.mytonwallet.R

class EntityHolder (view: View) : RecyclerView.ViewHolder(view) {
    val mvTitle: TextView
    val mvDesc1: TextView
    val mvDesc2: TextView
    val mvValue: TextView
    val mvValueUsd: TextView
    val mvIcon: ImageView
    val mvStakIcon: ImageView
    val mvIconSymbol: TextView
    val mvIconSingleLay: View
    val mvIconSwapLay: View
    val mvIconSwap1: ImageView
    val mvIconSwap2: ImageView
    val mvTitleSwapLay: View
    val mvTitleSwapText: TextView
    val mvIconNft: ImageView


    init {
        mvTitle = view.findViewById(R.id.it_jetton_title)
        mvDesc1 = view.findViewById(R.id.it_jetton_desc1)
        mvDesc2 = view.findViewById(R.id.it_jetton_desc2)
        mvValue = view.findViewById(R.id.it_jetton_value)
        mvValueUsd = view.findViewById(R.id.it_jetton_value_usd)
        mvIcon = view.findViewById(R.id.it_jetton_image_main)
        mvStakIcon = view.findViewById(R.id.it_jetton_image_staking)
        mvIconSymbol = view.findViewById(R.id.it_jetton_image_symbol)
        mvIconSingleLay = view.findViewById(R.id.it_jet_icon_single_layout)

        mvIconSwapLay = view.findViewById(R.id.it_jet_icon_swap_layout)
        mvIconSwap1 = view.findViewById(R.id.it_jetton_image_swap1)
        mvIconSwap2 = view.findViewById(R.id.it_jetton_image_swap2)
        mvTitleSwapLay = view.findViewById(R.id.it_jet_lay_swap_title)
        mvTitleSwapText = view.findViewById(R.id.it_jet_swap_title2)
        mvIconNft = view.findViewById(R.id.it_jet_nft_image)
    }
}