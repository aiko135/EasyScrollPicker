package ktepin.android.easyscrollpicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class EasyScrollViewHolder<I>(
    view: View,
) : RecyclerView.ViewHolder(view) {
    open fun decorateViewAtPos(relativePos: Int, item: I) { }
    open fun buildAnimations(): List<EasyScrollAnimation>{
        return listOf()
    }
}