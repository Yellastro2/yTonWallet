package com.yellastro.mytonwallet.pro_animations

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator

class ColorAnimation {

    companion object {
        @SuppressLint("RestrictedApi")
        fun animateColor(
            ctx: Context,
            fBaseColor: Int,
            fAnimColor: Int,
            fColoredOn: (Int) -> Unit,
            fOnEnd: () -> Unit
        ) {
            val colorFrom = ContextCompat.getColor(ctx, fAnimColor)
            val colorTo = ContextCompat.getColor(ctx, fBaseColor)
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorTo, colorFrom)
            colorAnimation.setDuration(50)
            colorAnimation.addUpdateListener { animator ->
                fColoredOn(animator.getAnimatedValue() as Int)
            }

            val colorAnimationBack = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            colorAnimationBack.setDuration(500)
            colorAnimationBack.addUpdateListener { animator ->
                fColoredOn(animator.animatedValue as Int)
            }
            colorAnimation.start()
            colorAnimationBack.doOnEnd {
                fOnEnd()
            }
            colorAnimation.doOnEnd {
                colorAnimationBack.start()
            }
        }
        @SuppressLint("RestrictedApi")
        fun animateColor_v1(ctx: Context,
                            fBaseColor: Int,
                            fAnimColor: Int,
                            fDuration: Long,
                            fColoredOn: (Int) -> Unit,
                            fOnEnd: () -> Unit){
            val colorFrom = ContextCompat.getColor(ctx, fAnimColor)
            val colorTo = ContextCompat.getColor(ctx, fBaseColor)
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(),colorTo, colorFrom)
            colorAnimation.setDuration(fDuration)
            colorAnimation.addUpdateListener { animator ->
                fColoredOn(animator.getAnimatedValue() as Int)
            }
            colorAnimation.doOnEnd {
                fOnEnd()
            }

            colorAnimation.start()

        }
    }



}