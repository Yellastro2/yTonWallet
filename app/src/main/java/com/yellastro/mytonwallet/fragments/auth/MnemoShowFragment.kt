package com.yellastro.mytonwallet.fragments.auth

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.MNEMO
import com.yellastro.mytonwallet.MNEMO_LIST
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import kotlinx.coroutines.launch
import kotlin.random.Random

class MnemoShowFragment : Fragment() {

    val MNEMO_SIZE = 24

    val mMnemo = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fWords = MNEMO_LIST.toMutableList()

        for (i in 0..<MNEMO_SIZE){
            val randomIndex = Random.nextInt(fWords.size);
            mMnemo.add(fWords[randomIndex])
            fWords.removeAt(randomIndex)
        }

        requireActivity()
            .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
            .edit()
            .putString(MNEMO,mMnemo.joinToString()).apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mnemo_show, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.fr_setpin_back).setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        val fvTable = view.findViewById<TableLayout>(R.id.fr_mnemoshow_table)
        val fRowParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        val fHalf = (MNEMO_SIZE / 2)
        for (i in 0..<fHalf){
            val qRow = TableRow(requireContext())
            qRow.layoutParams = fRowParams
            fvTable.addView(qRow)


            for (j in listOf(i,i+fHalf)){
                LayoutInflater.from(requireContext()).inflate(R.layout.mnemo_item_view, qRow)
                val qWordItem1: View = qRow.getChildAt(qRow.childCount-1)
                qWordItem1.findViewById<TextView>(R.id.mnemo_item_num).setText("${j+1}")
                qWordItem1.findViewById<TextView>(R.id.mnemo_item_word).setText(mMnemo[j])
            }
        }

        view.findViewById<View>(R.id.fr_mnemo_btn_done).setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.action_mnemoShowFragment_to_inputMnemoFragment)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(resources, R.drawable.webp_memo)
            lifecycleScope.launch {
                val drawable = ImageDecoder.decodeDrawable(source)
                val fvImage = view.findViewById<ImageView>(R.id.fr_showmnemo_image)
                fvImage.setImageDrawable(drawable)
                if (drawable is AnimatedImageDrawable) {
                    drawable.repeatCount = 0
                    drawable.start()
                    fvImage.setOnClickListener { drawable.start() }
                }

            }
        }
    }
}