package com.yellastro.mytonwallet.fragments

import android.R.attr.name
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.MNEMO
import com.yellastro.mytonwallet.MNEMO_LIST
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.yDialog
import kotlin.random.Random


class InputMnemoFragment : Fragment() {

    val MNEMO_SIZE = 24
    val REPEAT_COUNT = 3

    val mMnemoParts = ArrayList<Int>()
    lateinit var mMnemo: List<String>

    val mvInputs = ArrayList<AutoCompleteTextView>()

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        mMnemo = requireActivity()
            .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .getString(MNEMO,"")!!.split(",")

        for (i in 0..<REPEAT_COUNT){
            var qNext = 0
            do{
                qNext = Random.nextInt(MNEMO_SIZE)+1
            }while (qNext in mMnemoParts)
            mMnemoParts.add(qNext)
        }
        mMnemoParts.sort()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_mnemo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var fDesc = resources.getString(R.string.desc_repeat_mnemo)

        view.findViewById<View>(R.id.fr_setpin_back).setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        view.findViewById<View>(R.id.fr_input_mneno_btn_done).setOnClickListener {
            onInputDone()
        }


        val fvInputLay = view.findViewById<ViewGroup>(R.id.fr_input_mnemo_input_lay)

        for (qPart in mMnemoParts){
            fDesc = fDesc.replaceFirst("PART","<b>${qPart}</b>")

            LayoutInflater.from(requireContext()).inflate(R.layout.mnemo_input_item_view, fvInputLay)
            val qCurentCount = fvInputLay.childCount
            val qInputLay: View = fvInputLay.getChildAt(qCurentCount-1)

            qInputLay.findViewById<TextView>(R.id.it_mneno_input_num).setText("${qPart}")

            val qvInputText = qInputLay.findViewById<AutoCompleteTextView>(R.id.it_mneno_input_text)
            mvInputs.add(qvInputText)

            qvInputText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (MNEMO_LIST.any { qMnemoWord ->
                            qMnemoWord.startsWith(s.toString())
                        } || s.isEmpty())
                    {
                        qInputLay.setBackgroundResource(R.drawable.bkg_input_ligth)
                    }else{
                        qInputLay.setBackgroundResource(R.drawable.bkg_input_ligth_wrong)
                    }

                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            })

            ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_list_item_1,
                MNEMO_LIST).also { adapter ->
                qvInputText.setAdapter(adapter)
            }
            qvInputText.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->

                if ((keyEvent.action == KeyEvent.ACTION_UP) &&
                    (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if (qCurentCount < fvInputLay.childCount){
                        val qNextInput = mvInputs[qCurentCount]
                        getViewKeyboard(qNextInput)
                    }else{
                        onInputDone()
                    }

                    return@OnKeyListener true
                }
                return@OnKeyListener false
            })
        }

        view.findViewById<TextView>(R.id.fr_input_mnemo_desc).setText(Html.fromHtml(fDesc))

        getViewKeyboard(mvInputs[0])
    }

    fun onInputDone(){
        if (checkInput()){
            navController.popBackStack(R.id.welcomeFrag,true)
            navController.navigate(R.id.walletFragment)

        }else{
            yDialog(requireContext())
                .yCreate()
                .setOnclick(R.id.dial_btn_one,{ clearMnemo() })
                .setOnclick(R.id.dial_btn_thwo,{
                    navController.popBackStack()
                })
                .show()
        }
    }

    private fun clearMnemo() {
//        TODO("Not yet implemented")
    }

    private fun checkInput(): Boolean {
        for (i in 0..<mvInputs.size){
            val qInput = mvInputs[i]
            val qTypedWord = qInput.text.toString().trim()
            val qMnemoWord = mMnemo[mMnemoParts[i]-1].trim()
            if (qTypedWord != qMnemoWord)
                return true
        }
        return true
    }

    fun getViewKeyboard(fView: View){
        if (fView.requestFocus()) {
            val imm = requireContext().getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(fView, InputMethodManager.SHOW_IMPLICIT)
        }
    }

}