package com.yellastro.mytonwallet.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yellastro.mytonwallet.ASSETS
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.entitis.yEvent
import com.yellastro.mytonwallet.floatToPrint
import com.yellastro.mytonwallet.fragments.EventInfoFragment
import com.yellastro.mytonwallet.usdRates
import com.yellastro.mytonwallet.views.setTransAva

class HistoryAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface yHistoryEntity{
    }

    class yDateHistory(val date: String) : yHistoryEntity

    val TYPE_DATE = 1
    val TYPE_EVENT = 2

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

    fun addItem(fItem: yHistoryEntity){
        dataSet.add(fItem)
        notifyItemInserted(dataSet.size-1)
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





    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(aviewHolder: RecyclerView.ViewHolder, position: Int) {
        if (dataSet[position] is yEvent) {

            val viewHolder = aviewHolder as EntityHolder
            val fEntity = dataSet[position] as yEvent

            aviewHolder.itemView.setOnClickListener {
                mFragmentManager?.let { it1 -> EventInfoFragment()
                    .yShow(it1,"")
                    .setEvent(fEntity)}
            }

            viewHolder.mvTitle.text = fEntity.title
            val resources = viewHolder.mvValue.resources

            viewHolder.mvDesc2.visibility = View.GONE
            viewHolder.mvStakIcon.visibility =  View.GONE

            if (fEntity.type == yEvent.TRANS){
                viewHolder.mvTitleSwapLay.visibility = View.GONE
                viewHolder.mvIconNft.visibility = View.GONE

                setTransAva(fEntity.addressEntity!!, viewHolder)

                var fValuePrefix = ""
                var fSendOrRes = R.string.wrd_received
                if (fEntity.isSend){
                    fSendOrRes = R.string.wrd_send
                    viewHolder.mvValue.setTextColor(resources.getColor(R.color.black))
                    viewHolder.mvValueUsd.setTextColor(resources.getColor(R.color.grey_text))
                }else {
                    viewHolder.mvValue.setTextColor(resources.getColor(R.color.green))
                    viewHolder.mvValueUsd.setTextColor(resources.getColor(R.color.green))
                    fValuePrefix = "+"
                }

                viewHolder.mvValue.text = "${fValuePrefix}${floatToPrint(fEntity.value)} ${fEntity.symbol}"

                val fSendOr = resources.getString(fSendOrRes) + " · "+fEntity.time
                viewHolder.mvDesc1.text = fSendOr


                if (fEntity.symbol != "USD₮") {
                    var fRate = usdRates.get(fEntity.symbol) ?: 0F
                    var fUsdPrice = "?"
                    if (fRate != 0F) fUsdPrice = "$" + floatToPrint(fRate * fEntity.value)
                    viewHolder.mvValueUsd.text = fUsdPrice
                }

            }
            else if (fEntity.type == yEvent.SWAP){
                viewHolder.mvIconSingleLay.visibility = View.GONE
                viewHolder.mvIconSwapLay.visibility = View.VISIBLE
                viewHolder.mvTitleSwapLay.visibility = View.VISIBLE
                viewHolder.mvIconNft.visibility = View.GONE

                viewHolder.mvTitleSwapText.text = fEntity.symbolSwap


                viewHolder.mvValue.setTextColor(resources.getColor(R.color.green))
                viewHolder.mvValue.text = "+${floatToPrint(fEntity.valueSwap!!)} ${fEntity.symbolSwap!!}"


                val fSwapStr = resources.getString(R.string.wrd_swap) + " · "+fEntity.time
                viewHolder.mvDesc1.text = fSwapStr


                viewHolder.mvValueUsd.setTextColor(resources.getColor(R.color.grey_text))
                viewHolder.mvValueUsd.text = "-${floatToPrint(fEntity.value)} ${fEntity.symbol}"

                viewHolder.mvIconSwap1.load(ASSETS[fEntity.symbol]?.get(2))

                viewHolder.mvIconSwap2.load(ASSETS[fEntity.symbolSwap]?.get(2))
            }
            else { // NFT

                viewHolder.mvTitleSwapLay.visibility = View.GONE
                viewHolder.mvIconNft.visibility = View.VISIBLE

                setTransAva(fEntity.addressEntity!!, viewHolder)

                var fSendOrRes = R.string.wrd_received
                if (fEntity.isSend){
                    fSendOrRes = R.string.wrd_send
                    viewHolder.mvValue.setTextColor(resources.getColor(R.color.black))
                    viewHolder.mvValueUsd.setTextColor(resources.getColor(R.color.grey_text))
                }else {
                    viewHolder.mvValue.setTextColor(resources.getColor(R.color.green))
                    viewHolder.mvValueUsd.setTextColor(resources.getColor(R.color.green))
                }

                val fSendOr = resources.getString(fSendOrRes) + " NFT" + " · "+fEntity.time
                viewHolder.mvDesc1.text = fSendOr

                viewHolder.mvValue.text = fEntity.nftName
                viewHolder.mvValueUsd.text = fEntity.symbol
                viewHolder.mvIconNft.load(fEntity.nftImageLink)

            }
        } else {
            val viewHolder = aviewHolder as DateHolder
            viewHolder.mvTitle.text = (dataSet[position] as yDateHistory).date
        }





    }





    override fun getItemCount() = dataSet.size


    var mFragmentManager: FragmentManager? = null
    fun setFragManager(onClick: FragmentManager) {
        mFragmentManager = onClick
    }


}