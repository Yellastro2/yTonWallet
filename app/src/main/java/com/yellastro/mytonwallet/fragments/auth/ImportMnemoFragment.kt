package com.yellastro.mytonwallet.fragments.auth

import android.content.Context
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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.yellastro.mytonwallet.MNEMO
import com.yellastro.mytonwallet.MNEMO_LIST
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.views.InputMnemoView
import com.yellastro.mytonwallet.views.yDialog

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



    var mvInputs = ArrayList<InputMnemoView>()
    var mInputWords = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_import_mnemo, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        mvInputs = ArrayList<InputMnemoView>()

        var fDesc = resources.getString(R.string.desc_import_mnemo_desc)

        view.findViewById<TextView>(R.id.fr_import_mnemo_desc).setText(Html.fromHtml(fDesc))


        val fvButton = view.findViewById<View>(R.id.fr_import_mneno_btn_done)
        fvButton.setOnClickListener {
            onInputDone()
        }


        val fvInputLay = view.findViewById<ViewGroup>(R.id.fr_import_mnemo_input_lay)

        for (qPart in 1..MNEMO_SIZE){
            val qvInput = InputMnemoView(requireContext(),
                fvInputLay,
                qPart,
                {onInputDone()})
            mvInputs.add(qvInput)
            if (mInputWords.size >= qPart )
                qvInput.mvInput.setText(mInputWords[qPart-1])
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
            mvInputs[MNEMO_SIZE - 1].mvInput.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {
//                    fvScroll.scrollToDescendant(fvButton)
                }
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

        return view
    }

    override fun onResume() {
        super.onResume()
        for (qPart in 1..MNEMO_SIZE){
            if (mInputWords.size >= qPart ) {
                val qvInput = mvInputs[qPart-1]
                qvInput.mvInput.setText(mInputWords[qPart - 1])
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }

    fun onInputDone(){

        mInputWords = mvInputs.map { qObj ->
            qObj.mvInput.text.toString()
        } as ArrayList<String>


        if (checkInput()){
            requireActivity()
                .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                .edit()
                .putString(MNEMO,mInputWords.joinToString()).apply()

            findNavController().navigate(R.id.action_importMnemoFragment_to_setPinFragment)

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
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        if (!preferences.getBoolean("check_mnemo",true))
            return true
        return mvInputs.all { qObj ->
            MNEMO_LIST.any { qMnemo ->
                qMnemo == qObj.mvInput.text.toString()
            }
        }
    }
}