package com.yellastro.mytonwallet.fragments.send

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.yellastro.mytonwallet.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TransInputValueFragment : Fragment() {

    companion object {
        val ADDRESSTO = "addressTo"
    }

    private var mJetton: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mJetton = it.getString(TransInputAdrFragment.JETTON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trans_input_value, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fvToolbar = view.findViewById<Toolbar>(R.id.fr_trans_inpadr_toolbar)
        fvToolbar.setOnMenuItemClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return@setOnMenuItemClickListener true
        }
        fvToolbar.title = resources.getString(R.string.wrd_send) + " ${mJetton}"
    }
}