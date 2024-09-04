package com.yellastro.mytonwallet.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.entitis.yJetton
import com.yellastro.mytonwallet.floatToPrint
import com.yellastro.mytonwallet.fragments.auth.ConfirmPinFragment
import com.yellastro.mytonwallet.fragments.EventInfoFragment
import com.yellastro.mytonwallet.fragments.send.TransInputAdrFragment

class ChoseCurAdapter:
    RecyclerView.Adapter<EntityHolder>() {

    var dataSet = ArrayList<yJetton>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(fList: ArrayList<yJetton>) {
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
            mFragment?.let {
                val fArgs = Bundle()
                fArgs.putString(TransInputAdrFragment.JETTON,dataSet[position].symbol)
                findNavController(it)
                    .navigate(
                    R.id.action_transChoseCurFragment_to_transInputAdrFragment,
                    fArgs)}
        }

        viewHolder.mvStakIcon.visibility = View.GONE
        viewHolder.mvDesc2.visibility = View.GONE
        viewHolder.mvValue.visibility = View.GONE
        viewHolder.mvValueUsd.visibility = View.GONE


        viewHolder.mvTitle.text = dataSet[position].name
        viewHolder.mvDesc1.text =
            floatToPrint(dataSet[position].value.toDouble()," ") + " " + dataSet[position].symbol
        viewHolder.mvIcon.load(dataSet[position].image) {
            crossfade(true)
            placeholder(R.drawable.img_jet_holder)
        }

    }

    override fun getItemCount() = dataSet.size

    var mFragment: Fragment? = null
    fun setFragManager(onClick: Fragment): ChoseCurAdapter {
        mFragment = onClick
        return this
    }
}
