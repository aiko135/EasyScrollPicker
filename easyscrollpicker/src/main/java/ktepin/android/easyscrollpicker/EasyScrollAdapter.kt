package ktepin.android.easyscrollpicker

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class EasyScrollAdapter<VH : ViewHolder, I>(
    private val passedOnCreateViewHolder: (parent: ViewGroup) -> VH,
    private val passedOnBindViewHolder: (holder: VH, item: I) -> Unit,
    private var elemWidthPx: Int? = null
) : RecyclerView.Adapter<VH>() {
    private var items: List<I>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = passedOnCreateViewHolder(parent)
        elemWidthPx?.let { elemWidth ->
            holder.itemView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                width = elemWidth
            }
        }
        return holder
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: VH, position: Int) {
        items?.let {
            passedOnBindViewHolder(holder, items!![position])
        }
    }

    internal fun setItems(items: List<I>) {
        this.items = items
        notifyDataSetChanged()
    }

    internal fun getItemAtPos(pos:Int):I? = items?.getOrNull(pos)
}