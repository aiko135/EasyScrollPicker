package ktepin.android.easyscrollpicker

import android.animation.ValueAnimator

class EasyScrollAnimation(
    val startRelativePos: Int,
    val endRelativePos: Int,
    val mirrorAnimation: Boolean,
    val animator: ValueAnimator
)