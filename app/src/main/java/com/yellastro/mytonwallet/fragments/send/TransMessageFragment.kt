package com.yellastro.mytonwallet.fragments.send

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.floatToPrint
import com.yellastro.mytonwallet.fragments.PincodeFragment
import com.yellastro.mytonwallet.fragments.send.TransInputAdrFragment.Companion.JETTON
import com.yellastro.mytonwallet.viewmodels.TransMessageModel
import com.yellastro.mytonwallet.views.setTransAvaToViews

class TransMessageFragment : Fragment() {

    companion object {
        val AMOUNT_VALUE = "amount"
        val MESSAGE = "message"
        val IS_ENCRYPT = "isencrypt"
    }

    val viewModel: TransMessageModel by viewModels()

    lateinit var mvSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.mAmount = it.getDouble(AMOUNT_VALUE)
            viewModel.mJetton = it.getString(JETTON)!!
            viewModel.mAddress = it.getString(TransInputValueFragment.ADDRESSTO)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trans_message, container, false)
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fvToolbar = view.findViewById<Toolbar>(R.id.fr_trans_message_toolbar)

        fvToolbar.setOnMenuItemClickListener {
            findNavController().popBackStack(R.id.walletFragment,true)
            findNavController().navigate(R.id.walletFragment)
            return@setOnMenuItemClickListener true
        }
        fvToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        fvToolbar.title = resources.getString(R.string.wrd_send) + " ${viewModel.mJetton}"

        mvSwitch = view.findViewById(R.id.fr_trans_msg_swich_encrypt)


        mvSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                mvSwitch.thumbDrawable.setTint(resources.getColor(R.color.blue_white))
            else
                mvSwitch.thumbDrawable.setTint(resources.getColor(R.color.grey_uncheck))
        }
        mvSwitch.isChecked = false

        val fContact = viewModel.mContact!!

        if (fContact.name.isNullOrEmpty())
            view.findViewById<View>(R.id.fr_event_message_recipient_lay).visibility = View.GONE
        else{
            view.findViewById<TextView>(R.id.fr_event_message_recipient_value).text =
                fContact.title
            setTransAvaToViews(fContact,
                view.findViewById(R.id.fr_event_message_image),
                view.findViewById(R.id.fr_event_image_symbol))
        }
        val fAddr = fContact.address
        view.findViewById<TextView>(R.id.fr_event_message_address_value).text =
            fAddr.substring(0,4) + "..." + fAddr.substring(fAddr.length-4,
                fAddr.length)
        view.findViewById<TextView>(R.id.fr_event_message_amount_value).text =
            floatToPrint(viewModel.mAmount," ") + " " + viewModel.mJetton
        view.findViewById<TextView>(R.id.fr_event_message_fee_value).text = "0.01 TON"

        view.findViewById<TextView>(R.id.fr_trans_message_button_send).setOnClickListener {
            confirm()
        }


    }

    private fun confirm() {
        val fMsg = requireView().findViewById<TextView>(R.id.fr_trans_message_input).text.toString()

        arguments?.putString(MESSAGE,fMsg)
        arguments?.putBoolean(IS_ENCRYPT,mvSwitch.isChecked)
        arguments?.putString(PincodeFragment.TYPE,PincodeFragment.CONFIRM_TRANS)
        findNavController().navigate(R.id.action_transMessageFragment_to_pincodeFragment2,
            arguments)
    }
}