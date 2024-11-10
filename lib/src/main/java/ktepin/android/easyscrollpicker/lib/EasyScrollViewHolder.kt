package ktepin.android.easyscrollpicker.lib

import android.animation.ValueAnimator
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class EasyScrollViewHolder<I>(
    view: View,
) : RecyclerView.ViewHolder(view) {
    //Map: Integer is animation index, ValueAnimator is user's defined animation
    internal var animations: Map<Int, ValueAnimator> = mapOf()
    fun applyAnimations(animations: Map<Int, ValueAnimator>){
        animations.keys.forEach {
            if (it < 0)
                throw IndexOutOfBoundsException()
        }
        this.animations = animations
    }
    open fun decorateViewAtPos(relativePos: Int, item: I) { }
}