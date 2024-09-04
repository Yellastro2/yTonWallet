package com.yellastro.mytonwallet.fragments.send

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.adapters.floatToPrint
import com.yellastro.mytonwallet.fragments.send.TransMessageFragment.Companion.AMOUNT_VALUE
import com.yellastro.mytonwallet.viewmodels.InputValueModel
import com.yellastro.mytonwallet.viewmodels.sJettonsWallet
import com.yellastro.mytonwallet.views.ButtonStuff.Companion.setButtonActivate
import kotlin.math.round
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TransInputValueFragment : Fragment() {

    companion object {
        val ADDRESSTO = "addressTo"
    }

    private var mJetton: String? = null

    lateinit var mvUsdText: TextView
    lateinit var mvInput: EditText
    lateinit var mvSymbolText: TextView
    lateinit var mvButtonContin: Button

    var mUsdRate = Random.nextDouble() * 100

    val viewModel: InputValueModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mJetton = it.getString(TransInputAdrFragment.JETTON)
                                // TODO later made HashMap by jetton address
            viewModel.mJetton = sJettonsWallet.filter { it.symbol == mJetton }[0]
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


        mUsdRate = viewModel.mJetton!!.usdPrice.toDouble()

        val fvToolbar = view.findViewById<Toolbar>(R.id.fr_trans_inpval_toolbar)
        fvToolbar.setOnMenuItemClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return@setOnMenuItemClickListener true
        }
        fvToolbar.title = resources.getString(R.string.wrd_send) + " ${mJetton}"

        mvSymbolText = view.findViewById<TextView>(R.id.fr_trans_inpval_symbol)
        mvSymbolText.text = mJetton

        mvUsdText = view.findViewById(R.id.fr_trans_inpval_swich_text_usd)
        mvInput = view.findViewById(R.id.fr_trans_inpval_input)
        mvButtonContin = view.findViewById(R.id.fr_trans_inputval_btn_continue)


        view.findViewById<View>(R.id.fr_trans_inpval_btn_max).setOnClickListener {
            setMax()
        }

        view.findViewById<View>(R.id.it_jetton_image_staking).visibility = View.GONE
        view.findViewById<View>(R.id.it_jetton_desc2).visibility = View.GONE
        view.findViewById<View>(R.id.it_jetton_value).visibility = View.GONE
        view.findViewById<View>(R.id.it_jetton_value_usd).visibility = View.GONE

        view.findViewById<ImageView>(R.id.it_jetton_image_main)
            .load(viewModel.mJetton!!.image) {
            crossfade(true)
            placeholder(R.drawable.img_jet_holder)
        }
        view.findViewById<TextView>(R.id.it_jetton_title).text = viewModel.mJetton!!.name
        view.findViewById<TextView>(R.id.it_jetton_desc1)
            .text = floatToPrint(viewModel.mJetton!!.value, " ") + viewModel.mJetton!!.symbol


        mvInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.startsWith(".")){
                    mvInput.setText("0" +s.toString())
                    return
                }else if (s.startsWith("00")){
                    mvInput.setText(s.toString().removePrefix("0"))
                    return
                }
                if (s.isNullOrEmpty())
                    return setUsdCourse(0.0)
                val fValue = s.toString().toDouble()

                viewModel.mValue = fValue
                if (viewModel.isUsdValue)
                    viewModel.mValue = fValue / mUsdRate
                else
                    viewModel.mValue = fValue

                setUsdCourse(fValue)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        val fvSwichBtn = view.findViewById<View>(R.id.fr_trans_inpval_swich_lay)

        if (mJetton == "USD₮")
            fvSwichBtn.visibility = View.GONE
        else
            fvSwichBtn.setOnClickListener {
                swichValues()
            }

        mvButtonContin.setOnClickListener {
            onContinue()
        }

        setUsdCourse(1.0)
        setButtonActivate(mvButtonContin,false)
    }

    private fun onContinue() {
        arguments?.putDouble(AMOUNT_VALUE,viewModel.mValue)
        findNavController().navigate(R.id.action_transInputValueFragment_to_transMessageFragment,
            arguments)
    }

    fun Double.round(decimals: Int,isToLow: Boolean = false): Double {
        val fDecPow = Math.pow(10.0,decimals.toDouble())
        var fRounded = round(this * fDecPow)
        if (isToLow) fRounded -= 1
        return fRounded / fDecPow
    }

    private fun setMax() {
        var fValue = viewModel.mJetton!!.value
        if (viewModel.isUsdValue) {
            fValue = (fValue * mUsdRate).round(2)
        }
        mvInput.setText(fValue.toString())
    }

    private fun swichValues() {
        viewModel.isUsdValue = !viewModel.isUsdValue
        var fValueStr = mvInput.text.toString()
        if (fValueStr.isNullOrEmpty()) fValueStr = "1"
        var fValue = fValueStr.toDouble()
        fValue = if (viewModel.isUsdValue)  fValue  * mUsdRate else fValue / mUsdRate

        var fDec = if (viewModel.isUsdValue)  2 else 9
        val fRounded = fValue.round(fDec,true)
        mvInput.setText((fRounded).toString())
        mvSymbolText.text = if (viewModel.isUsdValue) "$" else  mJetton
    }


    fun setUsdCourse(fValue: Double){
        var fBodyHolder = "\$d"
        if (viewModel.isUsdValue) fBodyHolder = "d ${mJetton}"
        if (fValue == 0.0) {
            mvUsdText.text = fBodyHolder.replace("d", "0")
            setButtonActivate(mvButtonContin,false)
        }
        else{
            val fValRated = if (viewModel.isUsdValue) fValue / mUsdRate else fValue * mUsdRate

            val fWalletValue = viewModel.mJetton!!.value

            setButtonActivate(mvButtonContin, if (viewModel.isUsdValue) fValRated <= fWalletValue
                else fValue <= fWalletValue)

            mvUsdText.text = fBodyHolder.replace("d",
                floatToPrint(fValRated))
        }
    }


}