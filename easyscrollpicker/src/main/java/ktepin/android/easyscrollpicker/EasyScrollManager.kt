package ktepin.android.easyscrollpicker

import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView

/**
 * there are 2 generics used in the lib. This generics passed by user form the app level
 *    VH - view holder of the inner RecyclerView
 *    I - item, payload data type which is associated for each item of the inner RecyclerView
 */
class EasyScrollManager<in VH : RecyclerView.ViewHolder, in I>(
    /* Required arguments */
    val easyScrollPicker: EasyScrollPicker,
    onCreateViewHolder: (parent: ViewGroup) -> VH,
    onBindViewHolder: (holder: VH, item: I) -> Unit,

    /* Optional arguments */
    private val onItemSelected: ((item:I)->Unit)? = null,
    private val decorateViewHolderAtPos: ((holder: VH, scrollPosition: ViewHolderPos) -> Unit)? = null
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
            easyScrollPicker.configureLayoutManager(onItemSelected)

            easyScrollPicker.doOnPreDraw {
                setItemsCallback?.let { cb ->
                    cb.invoke()
                    setItemsCallback = null
                }
            }

            easyScrollPicker.measurePadding(elemWidth)
        }
    }

    fun setItems(items: List<I>) {
        val setItemsCb = {
            adapter!!.setItems(items)
            easyScrollPicker.applyInitPosition()
        }

        adapter?.let {
            setItemsCb.invoke()
        } ?: run {
            setItemsCallback = setItemsCb
        }
    }
}