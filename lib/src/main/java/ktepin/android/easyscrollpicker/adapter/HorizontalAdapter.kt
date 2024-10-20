package ktepin.android.easyscrollpicker.adapter

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class HorizontalAdapter<VH : ViewHolder, I>(
    passedOnBindViewHolder: (holder: VH, item: I) -> Unit,
    private val passedOnCreateViewHolder: (parent: ViewGroup) -> VH,
) : AbstractAdapter<VH, I>(passedOnBindViewHolder) {
    private var elemWidthPx: Int = -1

    override fun applyElemSize(sizePx: Int) {
        elemWidthPx = sizePx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = passedOnCreateViewHolder(parent)
        if (elemWidthPx >= 0){
            holder.itemView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                width = elemWidthPx
            }
            holder.itemView.requestLayout()
        }
        return holder
    }
}