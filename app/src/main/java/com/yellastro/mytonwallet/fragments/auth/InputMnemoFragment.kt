package com.yellastro.mytonwallet.fragments.auth

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.yellastro.mytonwallet.MNEMO
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.views.InputMnemoView
import com.yellastro.mytonwallet.views.yDialog
import kotlinx.coroutines.launch
import kotlin.random.Random

class InputMnemoFragment : Fragment() {

    val MNEMO_SIZE = 24
    val REPEAT_COUNT = 3

    val mMnemoParts = ArrayList<Int>()
    lateinit var mMnemo: List<String>

    val mvInputs = ArrayList<InputMnemoView>()

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
                qNext = Random.nextInt(MNEMO_SIZE) +1
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

            mvInputs.add(
                InputMnemoView(requireContext(),
                    fvInputLay,
                    qPart,
                    { onInputDone() })
            )

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(resources, R.drawable.webp_teach)
            lifecycleScope.launch {
                val drawable = ImageDecoder.decodeDrawable(source)
                val fvImage = view.findViewById<ImageView>(R.id.fr_input_mnemo_image)
                fvImage.setImageDrawable(drawable)
                if (drawable is AnimatedImageDrawable) {
                    drawable.repeatCount = 0
                    drawable.start()
                    fvImage.setOnClickListener { drawable.start() }
                }

            }
        }

        view.findViewById<TextView>(R.id.fr_input_mnemo_desc).setText(Html.fromHtml(fDesc))
        mvInputs[0]
            .getViewKeyboard(mvInputs[0].mvInput)


    }

    fun onInputDone(){
        if (checkInput()){
            navController.popBackStack(R.id.welcomeFrag,true)
            navController.navigate(R.id.walletFragment)

        }else{
            yDialog(requireContext())
                .yCreate()
                .setonFirstButtonClick { clearMnemo() }
                .setonSecondButtonClick { navController.popBackStack() }
                .show()
        }
    }

    private fun clearMnemo() {
//        TODO("Not yet implemented")
    }

    private fun checkInput(): Boolean {

        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        if (!preferences.getBoolean("check_mnemo",true))
            return true

        for (i in 0..<mvInputs.size){
            val qInput = mvInputs[i].mvInput
            val qTypedWord = qInput.text.toString().trim()
            val qMnemoWord = mMnemo[mMnemoParts[i]-1].trim()
            if (qTypedWord != qMnemoWord)
                return false
        }
        return true
    }



}