package com.yellastro.mytonwallet.fragments.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.viewmodels.ChoseCurModel
import com.yellastro.mytonwallet.views.yDecorator

class TransChoseCurFragment : Fragment() {

    val viewModel: ChoseCurModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trans_chose_cur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fvToolbar = view.findViewById<Toolbar>(R.id.fr_trans_chosecur_toolbar)

        (requireActivity() as AppCompatActivity).setSupportActionBar(fvToolbar)
        fvToolbar.setOnMenuItemClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return@setOnMenuItemClickListener true
        }

        val fvList = view.findViewById<RecyclerView>(R.id.fr_trans_chosecur_list)

        fvList.adapter = viewModel.mJettonAdapter.setFragManager(this)

        fvList.addItemDecoration(yDecorator.getDecorator(requireContext()))


    }

}