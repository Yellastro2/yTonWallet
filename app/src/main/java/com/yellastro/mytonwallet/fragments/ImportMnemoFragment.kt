package com.yellastro.mytonwallet.fragments

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.MNEMO_LIST
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.views.InputMnemoView
import com.yellastro.mytonwallet.yDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImportMnemoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImportMnemoFragment : Fragment() {
    val MNEMO_SIZE = 24

    lateinit var navController: NavController


    val mvInputs = ArrayList<InputMnemoView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_import_mnemo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        var fDesc = resources.getString(R.string.desc_import_mnemo_desc)

        view.findViewById<TextView>(R.id.fr_import_mnemo_desc).setText(Html.fromHtml(fDesc))

//        view.findViewById<View>(R.id.fr_setpin_back).setOnClickListener {
//            val navController = findNavController()
//            navController.popBackStack()
//        }

        val fvButton = view.findViewById<View>(R.id.fr_import_mneno_btn_done)
        fvButton.setOnClickListener {
            onInputDone()
        }

//        val fvScroll = view.findViewById<NestedScrollView>(R.id.fr_input_mnemo_scroll)


        val fvInputLay = view.findViewById<ViewGroup>(R.id.fr_import_mnemo_input_lay)

        for (qPart in 1..MNEMO_SIZE){
            mvInputs.add( InputMnemoView(requireContext(),
                            fvInputLay,
                            qPart,
                            {onInputDone()}))
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
            mvInputs[MNEMO_SIZE - 1].mvInput.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
//                    fvScroll.scrollToDescendant(fvButton)
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })


    }

    fun onInputDone(){
        if (true){ // TODO
            navController.popBackStack(R.id.welcomeFrag,true)
            navController.navigate(R.id.walletFragment)

        }else{
            yDialog(requireContext())
                .yCreate()
                .setonFirstButtonClick(R.string.btn_close) {  }
                .ySetTitle(R.string.title_dial_wrongmnemo)
                .setBody(R.string.body_dial_wrongmnemo)
                .show()
        }
    }

    private fun checkInput(): Boolean {
        return mvInputs.all { qObj ->
            MNEMO_LIST.any { qMnemo ->
                qMnemo == qObj.mvInput.text.toString()
            }
        }
    }
}