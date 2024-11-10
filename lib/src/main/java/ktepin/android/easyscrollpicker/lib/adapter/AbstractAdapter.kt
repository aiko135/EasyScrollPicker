package ktepin.android.easyscrollpicker.lib.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class AbstractAdapter<VH : ViewHolder, I>(
    private val passedOnBindViewHolder: (holder: VH, item: I) -> Unit,
) : RecyclerView.Adapter<VH>() {
    private var items: List<I>? = null

    abstract fun applyElemSize(sizePx: Int)

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