package com.yellastro.mytonwallet.fragments

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.yellastro.mytonwallet.MNEMO
import com.yellastro.mytonwallet.PIN
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.pro_animations.ColorAnimation
import com.yellastro.mytonwallet.viewmodels.NewPincodeModel
import com.yellastro.mytonwallet.views.PinView


class SetPinFragment : Fragment(R.layout.fragment_set_pin) {

    private var isConfirm: String? = null

    lateinit var mvPinDots: PinView
    lateinit var mvPinPlace: LinearLayout

    lateinit var mvTitle: TextView
    lateinit var mvDesc: TextView


    //  im sory about that EditText, i believe it culd be better way
//  to input text programmaticaly if just spend more time
    lateinit var mvInput: EditText
    lateinit var navController: NavController

    val viewModel: NewPincodeModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isConfirm = it.getString(ConfirmPinFragment.IS_CONFIRM)
            viewModel.mPinSize = isConfirm!!.length
        }

        navController = findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mvInput = view.findViewById(R.id.setpin_input)
        mvPinPlace = view.findViewById(R.id.pin_frag_dots)

        mvTitle = view.findViewById(R.id.fr_setpin_title)
        mvDesc = view.findViewById(R.id.fr_setpin_desc)

        val fvChangeSize = view.findViewById<TextView>(R.id.fr_setpin_btn_changesize)
        
        if (!isConfirm.isNullOrEmpty()){
            mvTitle.setText(R.string.title_conf_pin)
            val fDesc = resources.getString(R.string.deck_conf_pin).replace("4",viewModel.mPinSize.toString())
            mvDesc.setText(fDesc)
            fvChangeSize.visibility = View.INVISIBLE
        }else{
            val fDesc = resources.getString(R.string.deck_set_pin).replace("4",viewModel.mPinSize.toString())
            mvDesc.setText(fDesc)
        }

        view.findViewById<View>(R.id.fr_setpin_back).setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        (mvPinPlace.parent as View).setOnClickListener {
            yShowKeyboard()
        }




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

        yShowKeyboard()

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

        if (isConfirm.isNullOrEmpty()){
            val fArgs = Bundle()
            fArgs.putString(ConfirmPinFragment.IS_CONFIRM,viewModel.mPincode)
            navController.navigate(
                R.id.action_setPinFragment_self,
                fArgs)
        }else{
            if (isConfirm != viewModel.mPincode)
                wrong()
            else{
                correct()
            }
        }

    }

    fun correct(){
        animateColor(R.color.black,
            R.color.green,
            { fCol ->
                mvPinDots.setColor(fCol)
            },
            {
                nextStep()
            }
        )
    }

    private fun nextStep() {
        val fPref = requireActivity()
            .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

        fPref.edit()
            .putString(PIN,viewModel.mPincode).apply()


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val biometricManager = androidx.biometric.BiometricManager.from(requireContext())
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    // Device allow to use biometric, send user to bio screen.
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                    endWithPinAndBio(fPref,true)
                }

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    // Device unavaible, send user to mnemo screen.
                    Log.e("MY_APP_TAG", "No biometric features available on this device.")
                    endWithPinAndBio(fPref,false)
                }

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    // Device unavaible, send user to mnemo screen.
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                    endWithPinAndBio(fPref,false)
                }

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // Device allow to use biometric, send user to bio screen.
                    endWithPinAndBio(fPref,true)
                }
            }
        } else {
            endWithPinAndBio(fPref,false)
        }

    }

    fun endWithPinAndBio(fPref: SharedPreferences,isBio: Boolean){
        if (isBio){
            navController.navigate(R.id.action_setPinFragment_to_useBioFragment)
        }else {
            if (fPref.contains(MNEMO)) {
                navController.popBackStack(R.id.welcomeFrag, true)
                navController.navigate(R.id.walletFragment)
            } else {
                navController.navigate(R.id.action_setPinFragment_to_mnemoShowFragment)
            }
        }
    }

    fun wrong(){
        ColorAnimation.animateColor(requireContext(),
            R.color.black,
            R.color.red,
            { fCol -> mvTitle.setTextColor(fCol)
                mvDesc.setTextColor(fCol)
                mvPinDots.setColor(fCol)
            },
            { mvInput.setText("") }
            )
    }

    @SuppressLint("RestrictedApi")
    fun animateColor(fBaseColor: Int,
                     fAnimColor: Int,
                     fColoredOn: (Int) -> Unit,
                     fOnEnd: () -> Unit){
        val colorFrom = ContextCompat.getColor(requireContext(), fAnimColor)
        val colorTo = ContextCompat.getColor(requireContext(), fBaseColor)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(),colorTo, colorFrom)
        colorAnimation.setDuration(50)
        colorAnimation.addUpdateListener { animator ->
            fColoredOn(animator.getAnimatedValue() as Int)
        }

        val colorAnimationBack = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimationBack.setDuration(500)
        colorAnimationBack.addUpdateListener { animator ->
            fColoredOn(animator.getAnimatedValue() as Int)
        }
        colorAnimation.start()
        colorAnimation.doOnEnd {
            colorAnimationBack.doOnEnd {
                fOnEnd()
            }
            colorAnimationBack.start()
        }
    }
}