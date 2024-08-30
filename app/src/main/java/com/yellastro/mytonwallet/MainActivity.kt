package com.yellastro.mytonwallet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.yellastro.mytonwallet.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {



    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navController = findNavController(R.id.nav_host_fragment_content_main)


//        setSupportActionBar(binding.toolbar)


        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white))
        window.setNavigationBarColor(ContextCompat.getColor(this,R.color.white))
        val decor = window.decorView
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }

        if (!getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                .getString(MNEMO,"").isNullOrEmpty()){
            navController.popBackStack(R.id.welcomeFrag,true)
            navController.navigate(R.id.pincodeFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}