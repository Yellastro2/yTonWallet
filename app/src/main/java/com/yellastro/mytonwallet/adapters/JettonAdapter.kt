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

class JettonAdapter() :
    RecyclerView.Adapter<JettonAdapter.ViewHolder>() {


    var dataSet = ArrayList<yJetton>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(fList: ArrayList<yJetton>){
        dataSet = fList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mvTitle: TextView
        val mvDesc1: TextView
        val mvDesc2: TextView
        val mvValue: TextView
        val mvValueUsd: TextView
        val mvIcon: ImageView
        val mvStakIcon: ImageView


        init {
            mvTitle = view.findViewById(R.id.it_jetton_title)
            mvDesc1 = view.findViewById(R.id.it_jetton_desc1)
            mvDesc2 = view.findViewById(R.id.it_jetton_desc2)
            mvValue = view.findViewById(R.id.it_jetton_value)
            mvValueUsd = view.findViewById(R.id.it_jetton_value_usd)
            mvIcon = view.findViewById(R.id.it_jetton_image_main)
            mvStakIcon = view.findViewById(R.id.it_jetton_image_staking)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_jetton, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.mvTitle.text = dataSet[position].title
        viewHolder.mvDesc1.text = dataSet[position].usdPrice
        viewHolder.mvDesc2.text = dataSet[position].APY
        viewHolder.mvValue.text = dataSet[position].value
        viewHolder.mvValueUsd.text = dataSet[position].valueUsd
        viewHolder.mvIcon.load(dataSet[position].image)
        viewHolder.mvStakIcon.visibility =  if (dataSet[position].isStaking) View.VISIBLE else View.GONE
        viewHolder.mvDesc2.visibility = viewHolder.mvStakIcon.visibility
    }

    override fun getItemCount() = dataSet.size

}