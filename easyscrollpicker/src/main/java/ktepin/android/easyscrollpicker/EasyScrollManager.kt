package ktepin.android.easyscrollpicker

import android.view.ViewGroup
import androidx.core.view.doOnAttach
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
    private val onItemSelect: ((item: I) -> Unit)? = null,
    private val decorateViewHolderAtPos: ((holder: VH, scrollPosition: ViewHolderPos) -> Unit)? = null
) {
    private var adapter: EasyScrollAdapter<VH, I>? = null
    private var setItemsCallback: (() -> Unit)? = null

    init {
        easyScrollPicker.doOnAttach {
            easyScrollPicker.init(
                onCreateViewHolder = onCreateViewHolder,
                onBindViewHolder = onBindViewHolder,
                onItemSelect = onItemSelect
            )
        }

        easyScrollPicker.doOnLayout {
            //essential action - compute each elem size (Width)
            easyScrollPicker.measureElemWidth()
            easyScrollPicker.adapter.elemWidthPx = easyScrollPicker.requiredElemWidth

            easyScrollPicker.doOnPreDraw {
                setItemsCallback?.let { cb ->
                    cb.invoke()
                    setItemsCallback = null
                }
            }
            easyScrollPicker.measureAndApplyPadding()
        }
    }

    fun setDefaultPosition(position: Int) {

    }

    fun setItems(items: List<I>) {
        val setItemsCb = {
            easyScrollPicker.adapter.setItems(items as List<Nothing>)
            easyScrollPicker.applyInitPosition()
        }

        adapter?.let {
            setItemsCb.invoke()
        } ?: run {
            setItemsCallback = setItemsCb
        }
    }
}