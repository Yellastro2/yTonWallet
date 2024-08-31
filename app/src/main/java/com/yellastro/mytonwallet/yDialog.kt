package com.yellastro.mytonwallet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView

class yDialog(ctx: Context): Dialog(ctx) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        val window: Window = this.window!!
        window.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_layout)
        findViewById<View>(R.id.dial_btn_one).visibility = View.INVISIBLE
        findViewById<View>(R.id.dial_btn_thwo).visibility = View.INVISIBLE
    }

    fun yCreate(): yDialog {
        super.create()
        return this
    }

    fun ySetTitle(fTitle: Int): yDialog {
        findViewById<TextView>(R.id.dial_title).setText(fTitle)
        return this
    }

    fun setBody(fBody: Int): yDialog {
        findViewById<TextView>(R.id.dial_body).setText(fBody)
        return this
    }

    fun setonFirstButtonClick(fText: Int = 0,fOnClick: (View)->Unit): yDialog {
        val fBtn = findViewById<Button>(R.id.dial_btn_one)
        if (fText != 0) fBtn.setText(fText)
        fBtn.setOnClickListener { v ->
            this.dismiss()
            fOnClick(v)
        }
        fBtn.visibility = View.VISIBLE
        return this
    }

    fun setonSecondButtonClick(fOnClick: (View)->Unit): yDialog {
        val fBtn = findViewById<View>(R.id.dial_btn_thwo)
        fBtn.setOnClickListener { v ->
            this.dismiss()
            fOnClick(v)
        }
        fBtn.visibility = View.VISIBLE
        return this
    }
}