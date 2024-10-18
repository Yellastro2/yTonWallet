package com.yellastro.mytonwallet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
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

    @SuppressLint("SetJavaScriptEnabled")
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

        val myWebView = WebView(this)
        myWebView.webChromeClient = object : WebChromeClient() {

            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                Log.d("yTonWallet javascript", "${message.message()} -- From line " +
                        "${message.lineNumber()} of ${message.sourceId()}")
                return true
            }
        }
        myWebView.settings.javaScriptEnabled = true
        myWebView.addJavascriptInterface(WebAppInterface(this), "Android")
//        myWebView.loadData("<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Android!')\" />\n" +
//                "\n" +
//                "<script type=\"text/javascript\">\n" +
//                "    Android.showToast('toast');\n" +
//                "    \n" +
//                "</script>",null,null)
        myWebView.loadUrl("file:///android_asset/test.html")
    }

    /** Instantiate the interface and set the context.  */
    class WebAppInterface(private val mContext: Context) {

        /** Show a toast from the web page.  */
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }

}