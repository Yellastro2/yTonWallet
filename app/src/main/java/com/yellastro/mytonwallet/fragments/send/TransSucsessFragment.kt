package com.yellastro.mytonwallet.fragments.send

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.floatToPrint
import com.yellastro.mytonwallet.sAddressContact
import com.yellastro.mytonwallet.sJettonsWallet
import kotlinx.coroutines.launch

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

        val fvToolbar = view.findViewById<Toolbar>(R.id.fr_trans_success_toolbar)
        fvToolbar.setOnMenuItemClickListener {
            findNavController().navigate(R.id.action_transSucsessFragment_to_walletFragment)
            return@setOnMenuItemClickListener true
        }

        val fJettonEntity = sJettonsWallet.find { it.symbol == mJetton }!!
        val fContactEntity = sAddressContact.find { it.address == mAddress }!!

        view.findViewById<TextView>(R.id.fr_trans_succs_title).text =
            "${floatToPrint(mAmount," ")} ${mJetton}"

        val fUsdPrice = fJettonEntity.usdPrice * mAmount

        view.findViewById<TextView>(R.id.fr_trans_succs_usd_text).text =
            "\$${floatToPrint(fUsdPrice," ")}"

        val fDescBody = resources.getString(R.string.coins_been_send) + " ${fContactEntity.addressShort}"
        view.findViewById<TextView>(R.id.fr_trans_succs_desc).text = fDescBody
        view.findViewById<View>(R.id.fr_trans_succs_btn).setOnClickListener {
            findNavController().navigate(R.id.action_transSucsessFragment_to_walletFragment)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(resources, R.drawable.webp_party)
            lifecycleScope.launch {
                val drawable = ImageDecoder.decodeDrawable(source)
                val fvImage = view.findViewById<ImageView>(R.id.fr_trans_succs_image)
                fvImage.setImageDrawable(drawable)

                if (drawable is AnimatedImageDrawable) {
                    drawable.repeatCount = 0
                    drawable.start()
                    fvImage.setOnClickListener { drawable.start() }
                }

            }
        }
    }

}