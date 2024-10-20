package com.yellastro.mytonwallet.views

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatTextView

class MaxCharsTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
//        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                if (this@MaxCharsTextView.isInLayout)
//                    updateText()
//            }
//        })
        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateText()
        }

    }

    private var maxWordsPerLine: Int = 50
    private var isUpdated = false

    fun setMaxWordsPerLine(maxWords: Int) {
        maxWordsPerLine = maxWords
        updateText()
    }

    private fun updateText() {
        if (isUpdated)
            return
        isUpdated = true

//        val originalText = text.toString()
//        val words = originalText.replace("\n","").split(" ")
//        val stringBuilder = StringBuilder()
//
//        var index = 0
//        while (index < words.size) {
//            val endIndex = Math.min(index + maxWordsPerLine, words.size)
//            stringBuilder.append(words.subList(index, endIndex).joinToString(" "))
//            stringBuilder.append("\n ")
//            index = endIndex
//        }
//
//        text = stringBuilder.toString().trim()

        val originalText = SpannableStringBuilder(text)
        val words = originalText.toString().replace("\n", "").split(" ")
        val spannableStringBuilder = SpannableStringBuilder()

        var index = 0
        var spanOffset = 0
        while (index < words.size) {
            val endIndex = Math.min(index + maxWordsPerLine, words.size)
            val line = words.subList(index, endIndex).joinToString(" ")

            val spannableLine = SpannableStringBuilder(line)

            for (span in originalText.getSpans<Any>(spanOffset, spanOffset + line.length, Any::class.java)) {
                val spanStart = Math.max(0, originalText.getSpanStart(span) - spanOffset)
                val spanEnd = Math.min(line.length, originalText.getSpanEnd(span) - spanOffset)
                spannableLine.setSpan(span, spanStart -1, spanEnd -1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            spannableStringBuilder.append(spannableLine)
            if (endIndex < words.size) {
                spannableStringBuilder.append("\n ")
            }

            index = endIndex
            spanOffset += line.length + 1
        }

        text = spannableStringBuilder
        isUpdated = false

    }
}