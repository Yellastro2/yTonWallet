package com.yellastro.mytonwallet.views

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.yellastro.mytonwallet.R

class yDecorator {

    // TODO move to theme
    companion object {
        fun getDecorator(context: Context): MaterialDividerItemDecoration {
            val divider = MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL /*or LinearLayoutManager.HORIZONTAL*/)
            divider.setDividerInsetStart(context.resources.getDimension(R.dimen.margin_divider_76).toInt())
            divider.setLastItemDecorated(false)
            return divider
        }
    }
}