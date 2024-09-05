package com.yellastro.mytonwallet.fragments

import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.R
import kotlinx.coroutines.launch


class WelcomeFrag : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.welcome_frag_btn_create).setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_welcomeFrag_to_newCreateFragment)
        }

        view.findViewById<View>(R.id.welcome_frag_btn_import).setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_welcomeFrag_to_importMnemoFragment)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(resources,R.drawable.webp_crystal)
            lifecycleScope.launch {
                val drawable = ImageDecoder.decodeDrawable(source)
                view.findViewById<ImageView>(R.id.fr_wellcome_image).setImageDrawable(drawable)
                if (drawable is AnimatedImageDrawable) {
                    drawable.start()
                }
            }

//            imageView.setImageDrawable(drawable)



        }


//        startActivity(Intent(requireContext(),ScrollingActivity::class.java))
    }
}