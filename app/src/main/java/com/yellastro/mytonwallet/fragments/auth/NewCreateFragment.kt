package com.yellastro.mytonwallet.fragments.auth


import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.R
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewCreateFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_create, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.new_create_frag_btn).setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_newCreateFragment2_to_setPinFragment)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(resources, R.drawable.webp_party)
            lifecycleScope.launch {
                val drawable = ImageDecoder.decodeDrawable(source)
                val fvImage = view.findViewById<ImageView>(R.id.fr_newcreate_image)
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

