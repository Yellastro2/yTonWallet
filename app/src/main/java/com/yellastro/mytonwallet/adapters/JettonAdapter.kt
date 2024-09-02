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
import kotlin.math.abs
import kotlin.math.round

fun floatToPrint(fValue: Float): String {
    var fValueStr = ""
    if (fValue % 1F == 0.0F || abs(fValue) > 500){
        fValueStr = "%,d".format(fValue.toInt())
    }else if (abs(fValue) > 0.001){
//            fValueStr = "%,d".format(round(fValue * 100 ) / 100)
        fValueStr = (round(fValue * 100 ) / 100).toString()
    }else
//            fValueStr = "%,d".format(fValue)
        fValueStr = (fValue).toString()


    return fValueStr
}

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
        viewHolder.mvTitle.text = dataSet[position].title

        viewHolder.mvValue.text = "${floatToPrint(dataSet[position].value)} ${dataSet[position].symbol}"

        viewHolder.mvDesc1.text = "$"+floatToPrint(dataSet[position].usdPrice)
        viewHolder.mvDesc2.text = "APY "+floatToPrint(dataSet[position].APY)+"%"

        viewHolder.mvValueUsd.text = "$"+floatToPrint(dataSet[position].valueUsd)
        viewHolder.mvIcon.load(dataSet[position].image)

        if (dataSet[position].isStaking){
            viewHolder.mvStakIcon.visibility =  View.VISIBLE
            viewHolder.mvValue.setTextColor(viewHolder.mvValue.resources.getColor(R.color.green))
        }else{
            viewHolder.mvStakIcon.visibility = View.GONE
            viewHolder.mvValue.setTextColor(viewHolder.mvValue.resources.getColor(R.color.black))
            if (dataSet[position].priceChange != 0F){
                viewHolder.mvDesc2.visibility = View.VISIBLE
                var fPlusminus = "+"
                if (dataSet[position].priceChange > 0)
                    viewHolder.mvDesc2.setTextColor(viewHolder.mvValue.resources.getColor(R.color.green))
                else {
                    viewHolder.mvDesc2.setTextColor(
                        viewHolder.mvValue.resources.getColor(R.color.red))
                    fPlusminus = ""
                }
                viewHolder.mvDesc2.text = fPlusminus + floatToPrint(dataSet[position].priceChange) + "%"

            }else
                viewHolder.mvDesc2.visibility = View.GONE
        }

    }

    override fun getItemCount() = dataSet.size




}