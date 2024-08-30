package com.yellastro.mytonwallet.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewCreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_create, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewCreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewCreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.new_create_frag_btn).setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_newCreateFragment2_to_setPinFragment)
        }

//        some(view)
    }

    fun some(view: View){
//        val videolinkurl = "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Activity/Party%20Popper.webp"

        var videoView = view.findViewById(R.id.newcreate_video) as VideoView
        val imgHandler = view.findViewById<View>(R.id.imageView)

        // Set the media controller buttons
        var mediaController = MediaController(requireContext())
//        mediaController.setAnchorView(videoView)

        // Set MediaController for VideoView
        videoView.setMediaController(mediaController)
        mediaController.visibility = View.GONE
        val videolinkurl = "android.resource://com.yellastro.mytonwallet/" + R.raw.tada
//        videoView.requestFocus()
        videoView.setVideoURI(Uri.parse(videolinkurl))

//        videoView.setVideoPath()

//        val canSeekForward: Boolean = videoView.canSeekForward()
//        println(canSeekForward)
//        val canSeekBackward: Boolean = videoView.canSeekBackward()
//        println(canSeekBackward)


        // When the video file ready for playback.
//        videoView.setMediaController(null)
        // When the video file ready for playback.
        videoView.setOnPreparedListener {
            imgHandler.visibility = View.GONE
            videoView.visibility = View.VISIBLE
        }
        videoView.start()
//        imgHandler.visibility = View.GONE
        videoView.visibility = View.VISIBLE
//        videoView.setOnPreparedListener(OnPreparedListener {
//            videoView.seekTo(position)
//            if (position === 0) {
//                videoView.start()
//            } else if (canSeekForward && canSeekBackward) {
//                val currentPosition: Int = videoView.getCurrentPosition()
//                videoView.seekTo(currentPosition)
//                videoView.start()
//            } else {
//                videoView.pause()
//            }
//        })
//
//        videoView.setOnInfoListener(MediaPlayer.OnInfoListener { mediaPlayer, what, extra -> true })
//
//        videoView.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra -> true })
//
//        videoView.setOnCompletionListener(OnCompletionListener { videoView.stopPlayback() })
    }
}