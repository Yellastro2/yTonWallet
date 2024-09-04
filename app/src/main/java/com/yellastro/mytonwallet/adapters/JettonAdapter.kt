package com.yellastro.mytonwallet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.entitis.yJetton
import com.yellastro.mytonwallet.floatToPrint
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.abs
import kotlin.math.round







class JettonAdapter() :
    RecyclerView.Adapter<EntityHolder>() {


    var dataSet = ArrayList<yJetton>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(fList: ArrayList<yJetton>){
        dataSet = fList
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EntityHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_jetton, viewGroup, false)

        return EntityHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: EntityHolder, position: Int) {
        val resources = viewHolder.mvValue.resources

        viewHolder.mvTitle.text = dataSet[position].title
        
        val fValueStr = floatToPrint(dataSet[position].value).replace(","," ")
        viewHolder.mvValue.text = "${fValueStr} ${dataSet[position].symbol}"

        viewHolder.mvDesc1.text = "$"+floatToPrint(dataSet[position].usdPrice.toDouble())
        viewHolder.mvDesc2.text = "APY "+floatToPrint(dataSet[position].APY.toDouble())+"%"

        viewHolder.mvValueUsd.text = "$"+floatToPrint(dataSet[position].valueUsd.toDouble())
        viewHolder.mvIcon.load(dataSet[position].image) {
            crossfade(true)
            placeholder(R.drawable.img_jet_holder)
        }

        if (dataSet[position].isStaking){
            viewHolder.mvStakIcon.visibility =  View.VISIBLE
            viewHolder.mvValue.setTextColor(resources.getColor(R.color.green))
            viewHolder.mvDesc2.setTextColor(resources.getColor(R.color.green))
        }else{
            viewHolder.mvStakIcon.visibility = View.GONE
            viewHolder.mvValue.setTextColor(resources.getColor(R.color.black))
            if (dataSet[position].priceChange != 0F){
                viewHolder.mvDesc2.visibility = View.VISIBLE
                var fPlusminus = "+"
                if (dataSet[position].priceChange > 0)
                    viewHolder.mvDesc2.setTextColor(resources.getColor(R.color.green))
                else {
                    viewHolder.mvDesc2.setTextColor(
                        resources.getColor(R.color.red))
                    fPlusminus = ""
                }
                viewHolder.mvDesc2.text = fPlusminus + floatToPrint(dataSet[position].priceChange.toDouble()) + "%"

            }else
                viewHolder.mvDesc2.visibility = View.GONE
        }

    }

    override fun getItemCount() = dataSet.size




}