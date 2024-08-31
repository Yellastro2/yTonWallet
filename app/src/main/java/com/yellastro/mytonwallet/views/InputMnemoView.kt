package com.yellastro.mytonwallet.views

import android.content.Context
import android.text.Editable
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
import com.yellastro.mytonwallet.MNEMO_LIST
import com.yellastro.mytonwallet.R

class InputMnemoView(   val ctx: Context,
                        vParentView: ViewGroup,
                        part: Int,
                        onInputDone: ()-> Unit) {

    var mvInput: EditText


    init {
        LayoutInflater.from(ctx).inflate(R.layout.mnemo_input_item_view, vParentView)
        val qCurentCount = vParentView.childCount
        val qInputLay: View = vParentView.getChildAt(qCurentCount-1)

        qInputLay.findViewById<TextView>(R.id.it_mneno_input_num).setText("${part}")

        val qvInputText = qInputLay.findViewById<AutoCompleteTextView>(R.id.it_mneno_input_text)
//        mvInputs.add(qvInputText)
        mvInput = qvInputText

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

        ArrayAdapter<String>(ctx,
            android.R.layout.simple_list_item_1,
            MNEMO_LIST
        ).also { adapter ->
            qvInputText.setAdapter(adapter)
        }
        qvInputText.setOnKeyListener(View.OnKeyListener { view, i, keyEvent ->

            if ((keyEvent.action == KeyEvent.ACTION_UP) &&
                (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER))
            {
                if (qCurentCount < vParentView.childCount){

                    val qNextInput = vParentView.getChildAt(qCurentCount)
                        .findViewById<AutoCompleteTextView>(R.id.it_mneno_input_text)
                    getViewKeyboard(qNextInput)
                }else{
                    onInputDone()
                }

                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }

    fun getViewKeyboard(fView: View){
        if (fView.requestFocus()) {
            val imm = ctx.getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(fView, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}