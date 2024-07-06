package ktepin.android.easyscrollpicker

import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView

class EasyScrollManager<in VH : RecyclerView.ViewHolder, in I>(
    /* Required arguments */
    val easyScrollPicker: EasyScrollPicker,
    onCreateViewHolder: (parent: ViewGroup) -> VH,
    onBindViewHolder: (holder: VH, item: I) -> Unit,

    /* Optional arguments */
    private val decorateScrolledItem: ((holder: VH, scrollPosition: ItemScrolledPos) -> Unit)? = null
) {
    private var adapter: EasyScrollAdapter<VH, I>? = null
    private var onDrawCallback: (() -> Unit)? = null

    init {
        //TODO maybe in onLayout inside Picker
        easyScrollPicker.doOnLayout { easyScrollPickerView ->
            val elemWidth = easyScrollPicker.attributes.itemsOnScreen?.let {
                easyScrollPickerView.measuredWidth / it
            }

            this.adapter = EasyScrollAdapter(
                onCreateViewHolder,
                onBindViewHolder,
                elemWidth
            )
            easyScrollPicker.adapter = this.adapter

            onDrawCallback?.let { callback ->
                easyScrollPicker.doOnPreDraw {
                    callback.invoke()
                }
            }
        }
    }

    fun setItems(items: List<I>) {
        adapter?.setItems(items) ?: run {
            onDrawCallback = {
                adapter!!.setItems(items)
            }
        }
    }
}