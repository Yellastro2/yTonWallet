package com.yellastro.mytonwallet.fragments.send

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.adapters.floatToPrint
import com.yellastro.mytonwallet.entitis.yAddress
import com.yellastro.mytonwallet.entitis.yJetton
import com.yellastro.mytonwallet.viewmodels.sAddressContact
import com.yellastro.mytonwallet.viewmodels.sJettonsWallet

class TransSucsessFragment : Fragment() {

    var mAddress: String = ""
    var mJetton: String = ""
    var mAmount: Double  = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mAmount = it.getDouble(TransMessageFragment.AMOUNT_VALUE)
            mJetton = it.getString(TransInputAdrFragment.JETTON)!!
            mAddress = it.getString(TransInputValueFragment.ADDRESSTO)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trans_sucsess, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fJettonEntity = sJettonsWallet.find { it.symbol == mJetton }!!
        val fContactEntity = sAddressContact.find { it.address == mAddress }!!

        view.findViewById<TextView>(R.id.fr_trans_succs_title).text =
            "${floatToPrint(mAmount," ")} ${mJetton}"

        val fUsdPrice = fJettonEntity.usdPrice * mAmount

        view.findViewById<TextView>(R.id.fr_trans_succs_usd_text).text =
            "\$${floatToPrint(fUsdPrice," ")}"

        val fDescBody = resources.getString(R.string.coins_been_send) + " ${fContactEntity.addressShort}"
        view.findViewById<TextView>(R.id.fr_trans_succs_desc).text = fDescBody
        view.findViewById<View>(R.id.fr_trans_succs_btn)
    }

}