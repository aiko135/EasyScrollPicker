package ktepin.android.easyscrollpicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class EasyViewHolder<I>(
    view: View,
) : RecyclerView.ViewHolder(view) {

    open fun decorateViewAtPos(relativePos: Int, item: I) { }
    open fun animationStep(prevPos:Int, newPos:Int, animStep:Int){ }
}