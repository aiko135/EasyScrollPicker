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
    private var setItemsCallback: (() -> Unit)? = null

    init {
        easyScrollPicker.doOnLayout { easyScrollPickerView ->
            //essential value - computed elem size
            val elemWidth = easyScrollPicker.attributes.itemsOnScreen.let {
                easyScrollPickerView.measuredWidth / it
            }

            this.adapter = EasyScrollAdapter(
                onCreateViewHolder,
                onBindViewHolder,
                elemWidth
            )
            easyScrollPicker.adapter = this.adapter

            easyScrollPicker.doOnPreDraw {
                setItemsCallback?.let { cb ->
                    cb.invoke()
                    setItemsCallback = null
                }
            }

            val clipPadding:Int = easyScrollPicker.measuredWidth / 2 - (elemWidth / 2)
            easyScrollPicker.setPadding(clipPadding, 0, clipPadding, 0)
//            easyScrollPicker.doOnLayoutAfterConfigured(elemWidth)
        }
    }

    fun setItems(items: List<I>) {
        adapter?.let {
            setItems(items)
        } ?: run {
            setItemsCallback = {
                adapter!!.setItems(items)
            }
        }
    }
}