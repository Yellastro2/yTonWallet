package com.yellastro.mytonwallet.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yellastro.mytonwallet.BIO_LOGIN
import com.yellastro.mytonwallet.PIN
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.floatToPrint
import com.yellastro.mytonwallet.fragments.send.TransInputAdrFragment
import com.yellastro.mytonwallet.fragments.send.TransInputValueFragment
import com.yellastro.mytonwallet.fragments.send.TransMessageFragment
import com.yellastro.mytonwallet.pro_animations.ColorAnimation
import com.yellastro.mytonwallet.viewmodels.PincodeModel
import com.yellastro.mytonwallet.viewmodels.TransMessageModel
import com.yellastro.mytonwallet.views.PinView


class PincodeFragment : Fragment() {
    companion object{
        val TYPE = "type"
        val CONFIRM_TRANS = "confirm_trans"
        val LOGIN = "login"
    }

    val ROW_COUNT = 4
    val ROW_SIZE = 3
    var PIN_SIZE = 4


    lateinit var navController: NavController

    val viewModel: PincodeModel by viewModels()
    lateinit var mPin: String

    lateinit var mvPinDots: PinView

    var mTheme= R.style.Theme_primaryColors
    var mDotEmpty = R.drawable.bkg_pin_empty
    var mDotFill = R.drawable.bkg_pin_fill
    var mColorPrimary = 0
    var mColorBack = 0

    var mType = LOGIN
    var mAmount = 0.0
    var mJetton = ""
    var mAddress = ""

    val viewModelTrans: TransMessageModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            it.getString(TYPE)?.let {fType ->
                mType = fType
                viewModelTrans.mAmount = it.getDouble(TransMessageFragment.AMOUNT_VALUE)
                viewModelTrans.mJetton = it.getString(TransInputAdrFragment.JETTON)!!
                viewModelTrans.mAddress = it.getString(TransInputValueFragment.ADDRESSTO)!!
            }
        }

        navController = findNavController()



        mPin = requireContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .getString(PIN,"")!!

        PIN_SIZE = mPin.length
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (mType == CONFIRM_TRANS){
            mTheme = R.style.Theme_MyTonWallet
            mDotEmpty = R.drawable.bkg_pin_empty_dark
            mDotFill= R.drawable.bkg_pin_fill_dark
        }

        requireContext().getTheme().applyStyle(mTheme, true)


        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
        mColorPrimary = (typedValue.resourceId)

        theme.resolveAttribute(android.R.attr.background, typedValue, true)
        mColorBack = (typedValue.resourceId)

        return inflater.inflate(R.layout.fragment_pincode, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (mType == LOGIN){
            val window = requireActivity().window
            window.setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.blue_white))
            window.setNavigationBarColor(ContextCompat.getColor(requireActivity(),R.color.blue_white))
            window.decorView
                .setSystemUiVisibility(0)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                (requireView().parent.parent as View).setBackgroundResource(R.color.blue_white)
            }
        }
        viewModel.mPincode = ""
        mvPinDots.setDots(viewModel.mPincode.length)
    }

    override fun onPause() {
        super.onPause()
        if (mType == LOGIN) {
            val window = requireActivity().window
            window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.white))
            window.setNavigationBarColor(ContextCompat.getColor(requireActivity(), R.color.white))
            val decor = window.decorView
            decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            )
            requireContext().getTheme().applyStyle(R.style.Theme_MyTonWallet, true)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                (requireView().parent.parent as View).setBackgroundResource(R.color.white)
            }
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons(view)

        mvPinDots = PinView(requireContext(),
            view.findViewById(R.id.pin_frag_dots),
            PIN_SIZE,
            mDotEmpty,
            mDotFill
            )

        mvPinDots.setDots(viewModel.mPincode.length)

        if (mType == CONFIRM_TRANS){

            val fvToolbar = view.findViewById<Toolbar>(R.id.fr_pincode_toolbar)
            fvToolbar.visibility = View.VISIBLE
            fvToolbar.setTitle(R.string.title_confirm)
            fvToolbar.setOnMenuItemClickListener {
                findNavController().popBackStack(R.id.walletFragment,true)
                findNavController().navigate(R.id.walletFragment)
                return@setOnMenuItemClickListener true
            }
            fvToolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

            view.findViewById<View>(R.id.fr_pincode_image_lock).visibility

            val fBody = "${resources.getString(R.string.wrd_send)} " +
                    "${floatToPrint(viewModelTrans.mAmount," ")} ${viewModelTrans.mJetton}"
            view.findViewById<TextView>(R.id.fr_pincode_text_title).text = fBody

            var fDesc = "${resources.getString(R.string.wrd_to)} "
            if (!viewModelTrans.mContact?.name.isNullOrEmpty())
                fDesc += "${viewModelTrans.mContact?.name} "
            fDesc += "(${viewModelTrans.mContact?.addressShort})"
            view.findViewById<TextView>(R.id.fr_pincode_text_desc).text = fDesc

        }

    }


    var isAlreadyNavigate = false

    fun correct(){
        if (isAlreadyNavigate) return
        isAlreadyNavigate = true
        if (mType == CONFIRM_TRANS){
//            navController.popBackStack(R.id.walletFragment, true)
//            navController.navigate(R.id.transSucsessFragment,arguments)

            navController.navigate(R.id.action_pincodeFragment2_to_transSucsessFragment,arguments)
        }else {
//            navController.popBackStack(R.id.pincodeFragment, true)
//            navController.navigate(R.id.walletFragment)
            navController.navigate(R.id.action_pincodeFragment_to_walletFragment)
        }
    }

    private fun wrong() {
        ColorAnimation.animateColor(requireContext(),
            mColorPrimary,
            R.color.red,
            {fColor -> mvPinDots.setColor(fColor)},
            {
                mvPinDots.clear()
                mvPinDots.setDots(0)
                viewModel.mPincode = ""

            })


    }

    fun onPinEnter(fNum: Int){
        if (fNum == -1) {
            if (viewModel.mPincode.length > 0) {
                viewModel.mPincode = viewModel.mPincode.substring(0, viewModel.mPincode.length - 1)
            }
        }else{
            if (viewModel.mPincode.length < PIN_SIZE)
                viewModel.mPincode = viewModel.mPincode + "${fNum}"
            if (viewModel.mPincode.length == PIN_SIZE)
                return checkPin()
        }
        mvPinDots.setDots(viewModel.mPincode.length)
    }

    private fun checkPin() {
        if (mPin != viewModel.mPincode)
            wrong()
        else{
            ColorAnimation.animateColor_v1(requireContext(),
                mColorPrimary,
                mColorBack,
                300L,
                {fColor -> mvPinDots.setColor(fColor)},
                {
                   correct()
                })

        }
    }



    private fun initButtons(view: View) {



        val fPinGrid = view.findViewById<TableLayout>(R.id.pincode_frag_grid)
        val fChilds = fPinGrid.children.iterator()
        var qRow: ViewGroup = fPinGrid
        var j = 3
        val TEXT_BUTTONS = arrayOf("","ABC","DEF","GHI","JKL","MNO","PQRS","TUV","WXYZ","","","")
        for (i in 0..<ROW_COUNT * ROW_SIZE){

            if (j == ROW_SIZE) {
                qRow = (fChilds.next() as ViewGroup)
                j = 0
            }
            if (i != 11 && i != 9){
                var qNumValue = i+1
                var qTextValue = TEXT_BUTTONS[i]
                if (i == 10) {
                    qNumValue = 0
                }

                LayoutInflater.from(requireContext()).inflate(R.layout.btn_pin, qRow)
                val qPinBtn: View = qRow.getChildAt(qRow.childCount-1)

                qPinBtn.findViewById<TextView>(R.id.btn_pin_number)
                    .setText("${qNumValue}")
                qPinBtn.findViewById<TextView>(R.id.btn_pin_text)
                    .setText(qTextValue)

                qPinBtn.setOnClickListener { onPinEnter(qNumValue) }

            }else {

                var qImage = R.drawable.img_delete
                var qOnClick = { v: View -> onPinEnter(-1) }
                if (i == 9) {
                    qImage = R.drawable.img_finger
                    qOnClick = { v: View -> onBioAuthType(-1) }
                }


                LayoutInflater.from(requireContext()).inflate(R.layout.btn_pin_img, qRow)
                val qPinBtn: View = qRow.getChildAt(qRow.childCount-1)
                qPinBtn.findViewById<ImageView>(R.id.btn_pin_img)
                    .setImageResource(qImage)

                if (!requireContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                    .getBoolean(BIO_LOGIN,false) && i == 9) {
                    qPinBtn.visibility = View.INVISIBLE
                    qPinBtn.isClickable = false
                }

                qPinBtn.setOnClickListener(qOnClick)

            }


//            qRow.addView(qPinBtn)
            j ++
        }
    }

    private fun onBioAuthType(i: Int) {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Snackbar.make(requireView(),
                        "Authentication error: $errString", Snackbar.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    correct()
                    Snackbar.make(requireView(),
                        "Authentication succeeded!", Snackbar.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Snackbar.make(requireView(), "Authentication failed",
                        Snackbar.LENGTH_SHORT)
                        .show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }


}