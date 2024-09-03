package com.yellastro.mytonwallet.fragments.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.fragments.send.TransInputValueFragment.Companion.ADDRESSTO
import com.yellastro.mytonwallet.viewmodels.InputAdrModel
import com.yellastro.mytonwallet.views.yDecorator

class TransInputAdrFragment : Fragment() {
    private var mJetton: String? = null

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

        val fvList = view.findViewById<RecyclerView>(R.id.fr_trans_inputadr_list)
        fvList.adapter = viewModel.mJettonAdapter.setFragManager(this)
        fvList.addItemDecoration(yDecorator.getDecorator(requireContext()))

        if (viewModel.mJettonAdapter.dataSet.isEmpty())
            view.findViewById<View>(R.id.fr_trans_inputadr_recent_title).visibility = View.GONE

        val fvToolbar = view.findViewById<Toolbar>(R.id.fr_trans_inpadr_toolbar)
        fvToolbar.setOnMenuItemClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return@setOnMenuItemClickListener true
        }
        fvToolbar.title = resources.getString(R.string.wrd_send) + " ${mJetton}"

        view.findViewById<View>(R.id.fr_trans_inputadr_btn).setOnClickListener {
            val fAddressTo = view.findViewById<EditText>(R.id.fr_trans_inputadr_input).text.toString()
            arguments?.putString(ADDRESSTO,fAddressTo)
            findNavController().navigate(R.id.action_transInputAdrFragment_to_transInputValueFragment,
                arguments)
        }

    }

    companion object {
        val JETTON = "jetton"
    }

}