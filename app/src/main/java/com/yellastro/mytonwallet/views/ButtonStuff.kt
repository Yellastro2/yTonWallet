package com.yellastro.mytonwallet.views

import android.widget.Button
import com.yellastro.mytonwallet.R

class ButtonStuff {
    companion object{
        fun setButtonActivate(fvButton: Button, fIsActive: Boolean){
            fvButton.isClickable = fIsActive
            var fBkg = R.drawable.bkg_button_dis
            if (fIsActive)
                fBkg = R.drawable.bkg_button
            fvButton.setBackgroundResource(fBkg)
        }
    }
}