package ktepin.android.easyscrollpicker

import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.io.path.fileVisitor

class EasyScrollManager<in VH : RecyclerView.ViewHolder, in I>(
    /* Required arguments */
    val easyScrollPicker: EasyScrollPicker,
    onCreateViewHolder: (parent: ViewGroup) -> VH,
    onBindViewHolder: (holder: VH, item: I) -> Unit,

    /* Optional arguments */
    private val decorateScrolledItem: ((holder: VH, scrollPosition: ItemScrolledPos) -> Unit)? = null
) {
    private var adapter: EasyScrollAdapter<VH, I>? = null
    private var itemsToAdapter: List<I>? = null

    init {
        easyScrollPicker.doOnLayout {
            val elemWidth = it.measuredWidth / 5

            this.adapter = EasyScrollAdapter(
                onCreateViewHolder,
                onBindViewHolder,
                elemWidth
            )
            easyScrollPicker.adapter = this.adapter
            itemsToAdapter?.let {
                this.adapter!!.setItems(itemsToAdapter!!)
                itemsToAdapter = null
            }
        }
    }


    fun setItems(items: List<I>) {
         if (this.adapter == null){
             itemsToAdapter = items
         }else{
             this.adapter!!.setItems(items)
         }
    }
}