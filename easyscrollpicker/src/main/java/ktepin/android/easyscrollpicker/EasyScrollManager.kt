package ktepin.android.easyscrollpicker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class EasyScrollManager<in VH : RecyclerView.ViewHolder, in I>(
    /* Required arguments */
    easyScrollPicker: EasyScrollPicker,
    onCreateViewHolder: (parent: ViewGroup) -> VH,
    onBindViewHolder: (holder: VH, item: I) -> Unit,

    /* Optional arguments */
    private val decorateScrolledItem: ((holder: VH, scrollPosition: ItemScrolledPos) -> Unit)? = null
) {
    private var adapter: EasyScrollAdapter<VH, I>? = null

    init {
        this.adapter = EasyScrollAdapter(
            onCreateViewHolder,
            onBindViewHolder
        )
        easyScrollPicker.adapter = this.adapter
    }

    fun setItems(items: List<I>) {
        adapter?.setItems(items)
    }
}