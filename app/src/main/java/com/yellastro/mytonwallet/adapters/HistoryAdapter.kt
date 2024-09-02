package com.yellastro.mytonwallet.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.entitis.yEvent
import kotlin.random.Random


interface yHistoryEntity{}

class yDateHistory(val date: String) : yHistoryEntity

val TYPE_DATE = 1
val TYPE_EVENT = 2


class HistoryAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class DateHolder (view: View) : RecyclerView.ViewHolder(view) {
        val mvTitle: TextView
        init {
            mvTitle = view.findViewById(R.id.it_date_title)
        }
    }


    var dataSet = ArrayList<yHistoryEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(fList: ArrayList<yHistoryEntity>){
        dataSet = fList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return if (dataSet[position] is yDateHistory) TYPE_DATE else TYPE_EVENT
    }



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_EVENT) {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_jetton, viewGroup, false)
            return EntityHolder(view)
        } else {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_date, viewGroup, false)
            return DateHolder(view)
        }

    }

    val usdRates = mapOf<String,Float>("TON" to 8F,"NOT" to 0.01F)

    val gradientStore = mapOf("#E0A2F3" to "#D669ED","#FF885E" to "#FF516A")

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(aviewHolder: RecyclerView.ViewHolder, position: Int) {
        if (dataSet[position] is yEvent) {
            val viewHolder = aviewHolder as EntityHolder
            val fEntity = dataSet[position] as yEvent
            viewHolder.mvTitle.text = fEntity.title
            val resoursces = viewHolder.mvValue.resources

            if (fEntity.type == yEvent.TRANS){
                var fValuePrefix = ""
                var fSendOrRes = R.string.wrd_received
                if (fEntity.isSend){
                    fSendOrRes = R.string.wrd_send
                    viewHolder.mvValue.setTextColor(resoursces.getColor(R.color.black))
                }else {
                    viewHolder.mvValue.setTextColor(resoursces.getColor(R.color.green))
                    fValuePrefix = "+"
                }

                viewHolder.mvValue.text = "${fValuePrefix}${floatToPrint(fEntity.value)} ${fEntity.symbol}"

                val fSendOr = resoursces.getString(fSendOrRes) + " · "+fEntity.time
                viewHolder.mvDesc1.text = fSendOr


                if (fEntity.symbol != "USD₮") {
                    var fRate = usdRates.get(fEntity.symbol) ?: 0F
                    var fUsdPrice = "?"
                    if (fRate != 0F) fUsdPrice = "$" + floatToPrint(fRate * fEntity.value)
                    viewHolder.mvValueUsd.text = fUsdPrice
                }


                if (fEntity.image.isNullOrEmpty()){
                    val fGrad = gradientStore.toList()[Random.nextInt(gradientStore.size)]
                    val gd = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(Color.parseColor(fGrad.first),
                            Color.parseColor(fGrad.second))
                    )
                    gd.cornerRadius = 0f
                    viewHolder.mvIcon.setImageDrawable(gd)

                    viewHolder.mvIconSymbol.visibility = View.VISIBLE
                    viewHolder.mvIconSymbol.text = fEntity.letter

                }else{
                    viewHolder.mvIconSymbol.visibility = View.GONE
                    viewHolder.mvIcon.load(fEntity.image)
                }

                viewHolder.mvDesc2.visibility = View.GONE
                viewHolder.mvStakIcon.visibility =  View.GONE

            }
        } else {
            val viewHolder = aviewHolder as DateHolder
            viewHolder.mvTitle.text = (dataSet[position] as yDateHistory).date
        }




//        viewHolder.mvValue.text = "${floatToPrint(dataSet[position].value)} ${dataSet[position].symbol}"
//
//        viewHolder.mvDesc1.text = "$"+floatToPrint(dataSet[position].value * )
//        viewHolder.mvDesc2.text = "APY "+floatToPrint(dataSet[position].APY)+"%"
//
//        viewHolder.mvValueUsd.text = "$"+floatToPrint(dataSet[position].valueUsd)
//        viewHolder.mvIcon.load(dataSet[position].image)
//
//        if (dataSet[position].isStaking){
//            viewHolder.mvStakIcon.visibility =  View.VISIBLE
//            viewHolder.mvValue.setTextColor(viewHolder.mvValue.resources.getColor(R.color.green))
//        }else{
//            viewHolder.mvStakIcon.visibility = View.GONE
//            viewHolder.mvValue.setTextColor(viewHolder.mvValue.resources.getColor(R.color.black))
//            if (dataSet[position].priceChange != 0F){
//                viewHolder.mvDesc2.visibility = View.VISIBLE
//                var fPlusminus = "+"
//                if (dataSet[position].priceChange > 0)
//                    viewHolder.mvDesc2.setTextColor(viewHolder.mvValue.resources.getColor(R.color.green))
//                else {
//                    viewHolder.mvDesc2.setTextColor(
//                        viewHolder.mvValue.resources.getColor(R.color.red))
//                    fPlusminus = ""
//                }
//                viewHolder.mvDesc2.text = fPlusminus + floatToPrint(dataSet[position].priceChange) + "%"
//
//            }else
//                viewHolder.mvDesc2.visibility = View.GONE
//        }

    }

    override fun getItemCount() = dataSet.size



}