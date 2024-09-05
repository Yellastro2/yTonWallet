package com.yellastro.mytonwallet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.yellastro.mytonwallet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            enableEdgeToEdge()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_main)

        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.white))

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)

        if (!getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                .getString(MNEMO,"").isNullOrEmpty()){
            navController.popBackStack(R.id.welcomeFrag,true)
            navController.navigate(R.id.pincodeFragment)
        }
    }



}