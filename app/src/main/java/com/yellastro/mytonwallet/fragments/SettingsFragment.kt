package com.yellastro.mytonwallet.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.yellastro.mytonwallet.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}