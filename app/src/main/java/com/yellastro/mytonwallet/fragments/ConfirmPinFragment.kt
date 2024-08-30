package com.yellastro.mytonwallet.fragments

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.viewmodels.NewPincodeModel
import com.yellastro.mytonwallet.views.PinView


/*
im honestly try hard to reuse SetPinFragment.
but navController.popbackstack sick me of after 3 hour of tortures

 */

class ConfirmPinFragment : Fragment(R.layout.fragment_set_pin) {

    companion object {
        const val IS_CONFIRM = "isconfirm"
    }

    private var isConfirm: String? = null

    lateinit var mvPinDots: PinView
    lateinit var mvPinPlace: LinearLayout

    lateinit var mvTitle: TextView
    lateinit var mvDesc: TextView


    //  im sory about that EditText, i believe it culd be better way
//  to input text programmaticaly if just spend more time
    lateinit var mvInput: EditText

    val viewModel: NewPincodeModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isConfirm = it.getString(IS_CONFIRM)
            viewModel.mPinSize = isConfirm!!.length
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mvInput = view.findViewById(R.id.setpin_input)
        mvPinPlace = view.findViewById(R.id.pin_frag_dots)

        mvTitle = view.findViewById(R.id.fr_setpin_title)
        mvDesc = view.findViewById(R.id.fr_setpin_desc)

        mvTitle.setText(R.string.title_conf_pin)
        val fDesc = resources.getString(R.string.deck_conf_pin).replace("4",viewModel.mPinSize.toString())
        mvDesc.setText(fDesc)

        view.findViewById<View>(R.id.fr_setpin_back).setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        (mvPinPlace.parent as View).setOnClickListener {
            yShowKeyboard()
        }

        yShowKeyboard()


        mvInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.mPincode = s.toString()
                mvPinDots.setDots(viewModel.mPincode.length)
                if (viewModel.mPincode.length == viewModel.mPinSize){
                    confirmPin()
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        setSize(viewModel.mPinSize)
        val fvChangeSize = view.findViewById<TextView>(R.id.fr_setpin_btn_changesize)

        fvChangeSize.visibility = View.GONE
        fvChangeSize.setOnClickListener {

            var fNewText = 0
            if (viewModel.mPinSize == 4){
                viewModel.mPinSize = 6
                fNewText = R.string.use_4_pin
            }
            else {
                viewModel.mPinSize = 4
                fNewText = R.string.use_6_pin
            }
            fvChangeSize.setText(fNewText)
            setSize(viewModel.mPinSize)
        }

    }

    private fun yShowKeyboard() {
        if (mvInput.requestFocus()) {
            val imm = requireContext().getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(mvInput, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun setSize(fSize: Int){
        mvPinPlace.removeAllViews()
        mvPinDots = PinView(requireContext(),
            mvPinPlace,
            fSize,
            R.drawable.bkg_pin_empty_dark,
            R.drawable.bkg_pin_fill_dark
        )

        mvInput.filters = arrayOf(InputFilter.LengthFilter(fSize))

        mvInput.setText("")
    }

    fun confirmPin(){
        val navController = findNavController()
         if (isConfirm != viewModel.mPincode)
                wrong()
    }

    @SuppressLint("RestrictedApi")
    fun wrong(){
        mvInput.setText("")
        val colorFrom = ContextCompat.getColor(requireContext(),R.color.red)
        val colorTo = ContextCompat.getColor(requireContext(),R.color.black)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(),colorTo, colorFrom)
        colorAnimation.setDuration(150) // milliseconds
        colorAnimation.addUpdateListener { animator ->
            mvTitle.setTextColor(animator.getAnimatedValue() as Int)
            mvDesc.setTextColor(animator.getAnimatedValue() as Int)
        }
        val colorAnimationBack = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimationBack.setDuration(250)
        colorAnimationBack.addUpdateListener { animator ->
            mvTitle.setTextColor(animator.getAnimatedValue() as Int)
            mvDesc.setTextColor(animator.getAnimatedValue() as Int)
        }
        colorAnimation.start()
        colorAnimation.doOnEnd {
            colorAnimationBack.start()
        }
    }





}