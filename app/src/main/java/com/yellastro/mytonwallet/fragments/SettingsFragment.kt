package com.yellastro.mytonwallet.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.yellastro.mytonwallet.PREF_KEY
import com.yellastro.mytonwallet.R
import com.yellastro.mytonwallet.entitis.yAddress
import com.yellastro.mytonwallet.sAddressContact
import com.yellastro.mytonwallet.viewmodels.WalletModel


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)


        findPreference<Preference>("clear")
            ?.setOnPreferenceClickListener {

                sAddressContact = ArrayList<yAddress>()
//                findNavController().popBackStack()
                findNavController().popBackStack(R.id.walletFragment,true)
                findNavController().navigate(R.id.pincodeFragment)

                val viewModel: WalletModel by viewModels()
                viewModel.clearWallet()
                true
            }
        findPreference<Preference>("exit")
            ?.setOnPreferenceClickListener {
                requireContext().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                    .edit().clear().apply()
                findNavController().popBackStack(R.id.walletFragment,true)
                findNavController().navigate(R.id.welcomeFrag)
                true
            }
    }
}