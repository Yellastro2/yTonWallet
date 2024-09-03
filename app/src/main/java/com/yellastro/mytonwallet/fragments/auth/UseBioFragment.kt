package com.yellastro.mytonwallet.fragments.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.yellastro.mytonwallet.BIO_LOGIN
import com.yellastro.mytonwallet.MNEMO
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R

class UseBioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_use_bio, container, false)
    }

    lateinit var mPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPref = requireActivity()
            .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

        view.findViewById<View>(R.id.fr_use_bio_enable).setOnClickListener {
            ySetBio()
        }
        view.findViewById<View>(R.id.fr_use_bio_skip).setOnClickListener {
            endWithBio(mPref)

        }
    }

    private fun ySetBio() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                mPref.edit()
                    .putBoolean(BIO_LOGIN,true).apply()
                endWithBio(mPref)

            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                // not reachable po idee
                Log.e("MY_APP_TAG", "No biometric features available on this device.")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                // not reachable po idee
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG
                    )
                }
                startActivityForResult(enrollIntent, 555)
            }
        }
    }

    fun endWithBio(fPref: SharedPreferences){

        if (fPref.contains(MNEMO)) {
            navController.popBackStack(R.id.welcomeFrag, true)
            navController.navigate(R.id.walletFragment)
        } else {
            navController.navigate(R.id.action_useBioFragment_to_mnemoShowFragment)
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0){
            // no TODO make dialog request to retry enable bio or scip
        }else if (resultCode == 2){
            mPref.edit()
                .putBoolean(BIO_LOGIN,true).apply()
            endWithBio(mPref)
        }

    }
}