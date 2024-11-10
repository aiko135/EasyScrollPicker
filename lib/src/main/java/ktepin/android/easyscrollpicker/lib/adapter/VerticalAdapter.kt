package ktepin.android.easyscrollpicker.lib.adapter

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class VerticalAdapter<VH : ViewHolder, I>(
    passedOnBindViewHolder: (holder: VH, item: I) -> Unit,
    private val passedOnCreateViewHolder: (parent: ViewGroup) -> VH,
) : AbstractAdapter<VH, I>(passedOnBindViewHolder) {
    private var elemHeightPx: Int = -1

    override fun applyElemSize(sizePx: Int) {
        elemHeightPx = sizePx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = passedOnCreateViewHolder(parent)
        if (elemHeightPx >= 0){
            holder.itemView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                height = elemHeightPx
            }
            holder.itemView.requestLayout()
        }
        return holder
    }
}