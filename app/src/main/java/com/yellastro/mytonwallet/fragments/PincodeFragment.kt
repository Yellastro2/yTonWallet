package com.yellastro.mytonwallet.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.PIN
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.pro_animations.ColorAnimation
import com.yellastro.mytonwallet.viewmodels.PincodeModel
import com.yellastro.mytonwallet.views.PinView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

val ROW_COUNT = 4
val ROW_SIZE = 3
val PIN_SIZE = 4

class PincodeFragment : Fragment() {


    lateinit var navController: NavController

    val viewModel: PincodeModel by viewModels()
    lateinit var mPin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        val window = requireActivity().window

        window.setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.blue_white))
        window.setNavigationBarColor(ContextCompat.getColor(requireActivity(),R.color.blue_white))
        val decor = window.decorView
        decor.setSystemUiVisibility(0)

        mPin = requireContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .getString(PIN,"")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_pincode, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PincodeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PincodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    lateinit var mvPinDots: PinView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons(view)

        mvPinDots = PinView(requireContext(),
            view.findViewById(R.id.pin_frag_dots),
            PIN_SIZE
            )

        mvPinDots.setDots(viewModel.mPincode.length)

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
            ColorAnimation.animateColor(requireContext(),
                R.color.white,
                R.color.blue_white,
                {fColor -> mvPinDots.setColor(fColor)},
                {
                    navController.popBackStack(R.id.pincodeFragment,true)
                    navController.navigate(R.id.walletFragment)
                })

        }
    }

    private fun wrong() {
        ColorAnimation.animateColor(requireContext(),
            R.color.white,
            R.color.red,
            {fColor -> mvPinDots.setColor(fColor)},
            {
                mvPinDots.setDots(0)
                viewModel.mPincode = ""

            })


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
                if (i == 9){ qImage = R.drawable.img_finger }


                LayoutInflater.from(requireContext()).inflate(R.layout.btn_pin_img, qRow)
                val qPinBtn: View = qRow.getChildAt(qRow.childCount-1)
                qPinBtn.findViewById<ImageView>(R.id.btn_pin_img)
                    .setImageResource(qImage)

                qPinBtn.setOnClickListener(qOnClick)

            }


//            qRow.addView(qPinBtn)
            j ++
        }
    }

    override fun onPause() {
        super.onPause()
        val window = requireActivity().window

        window.setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.white))
        window.setNavigationBarColor(ContextCompat.getColor(requireActivity(),R.color.white))

        val decor = window.decorView
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}