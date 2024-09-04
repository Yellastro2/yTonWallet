package com.yellastro.mytonwallet.fragments.send

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.fragments.send.TransInputAdrFragment.Companion.JETTON

class TransMessageFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val param1 = it.getString(AMOUNT_VALUE)
            val param2 = it.getString(JETTON)
            val param3 = it.getString(TransInputValueFragment.ADDRESSTO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trans_message, container, false)
    }

    companion object {
        val AMOUNT_VALUE = "amount"
    }
}