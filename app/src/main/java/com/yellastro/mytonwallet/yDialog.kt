package com.yellastro.mytonwallet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window

class yDialog(ctx: Context): Dialog(ctx) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        val window: Window = this.window!!
        window.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_layout)
    }

    fun yCreate(): yDialog {
        super.create()
        return this
    }

    fun setOnclick(fId: Int,fOnClick: (View)->Unit): yDialog {
        findViewById<View>(fId).setOnClickListener  { v ->
            this.dismiss()
            fOnClick(v)
        }
        return this
    }
}