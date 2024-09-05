package com.yellastro.mytonwallet.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yellastro.mytonwallet.ASSETS
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.entitis.yEvent
import com.yellastro.mytonwallet.floatToPrint
import com.yellastro.mytonwallet.usdRates
import com.yellastro.mytonwallet.views.setTransAvaToViews
import kotlin.math.pow


class EventInfoFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_info, container, false)
    }

    fun yShow(it1: FragmentManager, s: String): EventInfoFragment {
        show(it1,s)
        return this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val width = resources.displayMetrics.widthPixels

        val height = dialog?.window!!.attributes.height

        val wlp = dialog?.window!!.attributes

        wlp.gravity = Gravity.BOTTOM
//        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        dialog?.window!!.attributes = wlp

        dialog?.window?.setLayout(
            width,
            height
        )
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog?.window?.setNavigationBarColor(ContextCompat.getColor(requireContext(),R.color.white))
        dialog?.window?.decorView?.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)

        view.findViewById<View>(R.id.fr_event_cross_btn).setOnClickListener {
            dismiss()
        }

        onViewDone(view)
    }

    var onViewDone = { v: View -> }

    fun setEvent(fEvent: yEvent): EventInfoFragment {
        onViewDone = {
            it.findViewById<TextView>(R.id.fr_event_date_value).text = fEvent.dateFull
            it.findViewById<View>(R.id.fr_event_linkto_exp).setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fEvent.transLink)))
            }
            val fFee = fEvent.fee / (10.0.pow(9))
            it.findViewById<TextView>(R.id.fr_event_text_fee).text = floatToPrint(fFee)


            if (fEvent.type == yEvent.TRANS){
                it.findViewById<View>(R.id.fr_event_trans_lay).visibility = View.VISIBLE
                it.findViewById<View>(R.id.fr_event_lay_to).visibility = View.GONE
                it.findViewById<View>(R.id.fr_event_lay_swap_title).visibility = View.GONE
                it.findViewById<View>(R.id.fr_event_lay_nft_title).visibility = View.GONE


                var fSendOrRes = R.string.wrd_received
                var fFromOrTo = R.string.wrd_from
                var fValuePrefix = "+"

                val fvAmount = it.findViewById<TextView>(R.id.fr_event_amount_text)
                if (fEvent.isSend) {
                    fSendOrRes = R.string.wrd_send
                    fValuePrefix = "-"
                    fvAmount.setTextColor(resources.getColor(R.color.black))
                    fFromOrTo = R.string.wrd_to
                }
                val fStrSendOrRes = resources.getString(fSendOrRes)
                it.findViewById<TextView>(R.id.fr_event_from_title).setText(fFromOrTo)
                it.findViewById<TextView>(R.id.fr_event_title).text = fStrSendOrRes
                it.findViewById<TextView>(R.id.fr_event_date_title)
                    .setText(fStrSendOrRes + " " + resources.getString(R.string.wrd_at))

                fvAmount.text = "${fValuePrefix}${floatToPrint(fEvent.value, " ")} ${fEvent.symbol}"

                if (fEvent.symbol != "USD₮") {
                    var fRate = usdRates.get(fEvent.symbol) ?: 0F
                    var fUsdPrice = "?"
                    if (fRate != 0F) fUsdPrice = "$" + floatToPrint(fRate * fEvent.value, " ")
                    it.findViewById<TextView>(R.id.fr_event_amount_usd).text = fUsdPrice
                }else{
                    it.findViewById<TextView>(R.id.fr_event_amount_usd).visibility = View.INVISIBLE
                }

                val fvLayMessage = it.findViewById<View>(R.id.fr_event_message_lay)
                if (!fEvent.message.isNullOrEmpty()){
                    fvLayMessage.visibility = View.VISIBLE
                    val fvMessage = it.findViewById<TextView>(R.id.fr_event_message_text)
                    val fvDecrypt = it.findViewById<View>(R.id.fr_event_btn_decrypt)
                    if (fEvent.isEncrypt){

                        fvMessage.setText(R.string.body_msg_encrypt)
                        fvMessage.setTextColor(resources.getColor(R.color.grey_text))

                        fvDecrypt.setOnClickListener {
                            fvDecrypt.visibility = View.GONE
                            fvMessage.text = fEvent.message
                            fvMessage.setTextColor(resources.getColor(R.color.black))
                        }
                        fvDecrypt.visibility = View.VISIBLE
                    } else{
                        fvDecrypt.visibility = View.GONE
                        fvMessage.setTextColor(resources.getColor(R.color.black))

                        fvMessage.text = fEvent.message
                    }
                }else {
                    fvLayMessage.visibility = View.GONE
                }

                it.findViewById<TextView>(R.id.fr_event_from_value).text = fEvent.title

                setTransAvaToViews(fEvent.addressEntity!!,
                    it.findViewById(R.id.fr_event_from_image),
                    it.findViewById(R.id.fr_event_image_symbol))

                it.findViewById<TextView>(R.id.fr_event_text_amount_title).setText(R.string.title_amount)
                it.findViewById<TextView>(R.id.fr_event_amount_value).text =
                     "${floatToPrint(fEvent.value, " ")} ${fEvent.symbol}"





            }
            else if (fEvent.type == yEvent.NFT){

                it.findViewById<View>(R.id.fr_event_trans_lay).visibility = View.GONE
                it.findViewById<View>(R.id.fr_event_lay_to).visibility = View.VISIBLE
                it.findViewById<View>(R.id.fr_event_lay_swap_title).visibility = View.GONE
                it.findViewById<View>(R.id.fr_event_from_image).visibility = View.GONE
                it.findViewById<View>(R.id.fr_event_lay_nft_title).visibility = View.VISIBLE
                it.findViewById<View>(R.id.fr_event_lay_to).visibility = View.GONE
                it.findViewById<View>(R.id.fr_dial_lay_amount).visibility = View.GONE


                var fSendOrRes = R.string.wrd_received
                var fFromOrTo = R.string.wrd_from
                if (fEvent.isSend) {
                    fSendOrRes = R.string.wrd_send
                    fFromOrTo = R.string.wrd_to
                }
                val fStrSendOrRes = resources.getString(fSendOrRes)
                it.findViewById<TextView>(R.id.fr_event_date_title)
                    .setText(fStrSendOrRes + " " + resources.getString(R.string.wrd_at))

                val fBody = fStrSendOrRes + " NFT"

                it.findViewById<TextView>(R.id.fr_event_from_title).setText(fFromOrTo)

                it.findViewById<TextView>(R.id.fr_event_title).text = fBody

                it.findViewById<TextView>(R.id.fr_event_titled_nft_text1).text = fEvent.nftName
                it.findViewById<TextView>(R.id.fr_event_titled_nft_text2).text = fEvent.symbol


                it.findViewById<TextView>(R.id.fr_event_from_value).text = fEvent.title


                it.findViewById<ImageView>(R.id.it_jet_icon_nft).load(fEvent.nftImageLink)
            }
            else { //SWAP
                it.findViewById<View>(R.id.fr_event_trans_lay).visibility = View.GONE
                it.findViewById<View>(R.id.fr_event_lay_to).visibility = View.VISIBLE
                it.findViewById<View>(R.id.fr_event_lay_swap_title).visibility = View.VISIBLE
                it.findViewById<View>(R.id.fr_event_from_image).visibility = View.GONE
                it.findViewById<View>(R.id.fr_event_lay_nft_title).visibility = View.GONE

                it.findViewById<TextView>(R.id.fr_event_date_title)
                    .setText(resources.getString(R.string.wrd_swap) + " " + resources.getString(R.string.wrd_at))

                it.findViewById<TextView>(R.id.fr_event_title).setText(R.string.wrd_swap)

                it.findViewById<ImageView>(R.id.fr_event_image_swap1).load(ASSETS[fEvent.symbol]?.get(2))
                it.findViewById<ImageView>(R.id.fr_event_image_swap2).load(ASSETS[fEvent.symbolSwap]?.get(2))

                val fValueSend = "-${floatToPrint(fEvent.value," ")} ${fEvent.symbol}"
                val fValueReceive = "+${floatToPrint(fEvent.valueSwap!!," ")} ${fEvent.symbolSwap!!}"

                it.findViewById<TextView>(R.id.fr_event_titled_swap1_text).text = fValueSend
                it.findViewById<TextView>(R.id.fr_event_titled_swap2_text).text = fValueReceive
                it.findViewById<TextView>(R.id.fr_event_titled_swap1_text).setOnClickListener {
                    Toast.makeText(requireContext(),
                        "its hardly hurt my eyes when those digits was in dif margin cuz its dif size of + and - symb, so i prefer autor forgot to fix this margins",
                        Toast.LENGTH_LONG)
                        .show()
                }

                it.findViewById<TextView>(R.id.fr_event_from_title).setText(R.string.wrd_send)
                it.findViewById<TextView>(R.id.fr_event_from_value).text = fValueSend
                it.findViewById<TextView>(R.id.fr_event_receive_val_title).setText(R.string.wrd_received)
                it.findViewById<TextView>(R.id.fr_event_receive_val_value).text = fValueReceive

                val fPricePerTitle = resources.getString(R.string.title_price_per) + " " + fEvent.symbol
                it.findViewById<TextView>(R.id.fr_event_text_amount_title).text = fPricePerTitle


                val fPricePer = "${floatToPrint(fEvent.valueSwap!! / fEvent.value," ")} ${fEvent.symbol}"
                it.findViewById<TextView>(R.id.fr_event_amount_value).text = fPricePer


            }

        }


        return this
    }
}