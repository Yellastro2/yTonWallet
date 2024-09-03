package com.yellastro.mytonwallet.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.entitis.yAddress
import com.yellastro.mytonwallet.viewmodels.mDayFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone

class ContactAdapter :
    RecyclerView.Adapter<EntityHolder>() {

    var dataSet = ArrayList<yAddress>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(fList: ArrayList<yAddress>) {
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

        viewHolder.itemView.setOnClickListener {
//            mFragment?.let {
//                val fArgs = Bundle()
//                fArgs.putString(TransInputAdrFragment.JETTON,dataSet[position].symbol)
//                NavHostFragment.findNavController(it)
//                    .navigate(
//                        R.id.action_transChoseCurFragment_to_transInputAdrFragment,
//                        fArgs)}
        }

        viewHolder.mvStakIcon.visibility = View.GONE
        viewHolder.mvDesc2.visibility = View.GONE
        viewHolder.mvValue.visibility = View.GONE
        viewHolder.mvValueUsd.visibility = View.GONE


        setTransAvaToViews(dataSet[position], viewHolder.mvIcon, viewHolder.mvIconSymbol)

        viewHolder.mvTitle.text = dataSet[position].title

        val fDate = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dataSet[position].lastDate * 1000L),
            TimeZone.getDefault().toZoneId()
        )
        viewHolder.mvDesc1.text = mDayFormat.format(fDate)

        setTransAva(dataSet[position], viewHolder)

    }

    override fun getItemCount() = dataSet.size

    var mFragment: Fragment? = null
    fun setFragManager(onClick: Fragment): ContactAdapter {
        mFragment = onClick
        return this
    }
}