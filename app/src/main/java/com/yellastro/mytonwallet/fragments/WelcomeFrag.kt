package com.yellastro.mytonwallet.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.R


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
//        startActivity(Intent(requireContext(),ScrollingActivity::class.java))
    }
}