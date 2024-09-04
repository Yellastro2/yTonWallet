package com.yellastro.mytonwallet.fragments.send

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.fragments.send.TransInputValueFragment.Companion.ADDRESSTO
import com.yellastro.mytonwallet.viewmodels.InputAdrModel
import com.yellastro.mytonwallet.views.ButtonStuff.Companion.setButtonActivate
import com.yellastro.mytonwallet.views.yDecorator

class TransInputAdrFragment : Fragment() {
    private var mJetton: String? = null

    lateinit var mvButtonContin: Button

    val viewModel: InputAdrModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mJetton = it.getString(JETTON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trans_input_adr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fvToolbar = view.findViewById<Toolbar>(R.id.fr_trans_inpadr_toolbar)
        fvToolbar.setOnMenuItemClickListener {
            findNavController().popBackStack(R.id.walletFragment,true)
            findNavController().navigate(R.id.walletFragment)
            return@setOnMenuItemClickListener true
        }
        fvToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        fvToolbar.title = resources.getString(R.string.wrd_send) + " ${mJetton}"

        val fvInput = view.findViewById<EditText>(R.id.fr_trans_inputadr_input)

        fvInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                setButtonActivate(mvButtonContin, !s.isEmpty())
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        val fvList = view.findViewById<RecyclerView>(R.id.fr_trans_inputadr_list)
        fvList.adapter = viewModel.mJettonAdapter.setOnItemClick {
            fvInput.setText(it)
        }
        fvList.addItemDecoration(yDecorator.getDecorator(requireContext()))

        if (viewModel.mJettonAdapter.dataSet.isEmpty())
            view.findViewById<View>(R.id.fr_trans_inputadr_recent_title).visibility = View.GONE



        mvButtonContin = view.findViewById<Button>(R.id.fr_trans_inputadr_btn)
        mvButtonContin.setOnClickListener {
            val fAddressTo = fvInput.text.toString()
            arguments?.putString(ADDRESSTO,fAddressTo)
            findNavController().navigate(R.id.action_transInputAdrFragment_to_transInputValueFragment,
                arguments)
        }

        setButtonActivate(mvButtonContin,false)

    }

    companion object {
        val JETTON = "jetton"
    }

}