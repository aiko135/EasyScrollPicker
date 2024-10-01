package ktepin.android.easyscrollpicker

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * inspired by
 * https://github.com/adityagohad/HorizontalPicker/blob/master/horizontalpickerlib/src/main/java/travel/ithaka/android/horizontalpickerlib/PickerLayoutManager.java
 */
class CustomLayoutManager<I>(
    private val easyScrollPicker: EasyScrollPicker,
    orientation: Int,
    reverseLayout: Boolean,
    private val onItemSelect: ((item:I)->Unit)?,
    private val onItemChangeRelativePos: ((View:View, relativePos:Int)->Unit)?
) : LinearLayoutManager(easyScrollPicker.context, orientation, reverseLayout) {

    private val viewMap = mutableMapOf<View, Int>()
    private val adapter = easyScrollPicker.adapter as EasyScrollAdapter<*,I>
    private var lastSelectedPos = -1

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        onItemChangeRelativePos?.let {
            observeOnChange()
        }
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            onItemChangeRelativePos?.let {
                observeOnChange()
            }
            scrolled
        } else {
            0
        }
    }

    private fun observeOnChange() {
        val mid = width / 2.0f
        val elemWidth = getChildAt(0)?.width
            ?: return //all containers have the same width, so we can take the width of the first one

        var i = 0
        while (i < childCount) {

            getChildAt(i)?.let { child ->
                // Calculating the distance of the child from the center
                val childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f
                val distanceFromScreenCenter = abs(mid - childMid)

                if (distanceFromScreenCenter < elemWidth / 2) {
                    //got the central element
                    onItemSelect?.let {
                        select(child)
                    }

                    for (j in -i..-1)
                        checkIfItemPosChanged(getChildAt(i+j), j) //LEFT ITEMS

                    checkIfItemPosChanged(child, 0) //CENTRAL ITEM

                    for (k in 1..childCount)
                        checkIfItemPosChanged(getChildAt(i+k), k) //RIGHT ITEMS

                    i = childCount + 1 //BREAK CYCLE while (i < childCount) // classic break can be problem in lambdas
                }
            }
            i++
        }
    }

    private fun checkIfItemPosChanged(view:View?, relativePos:Int){
        view?.let { v ->
            if (viewMap.size > VIEW_MAP_LIMIT)
                viewMap.clear()

            viewMap[v]?.let {
                if (it != relativePos){
                    viewMap[v] = relativePos
                    onItemChangeRelativePos?.invoke(v, relativePos)
                }
            } ?:run {
                viewMap[v] = relativePos
                onItemChangeRelativePos?.invoke(v, relativePos)
            }
        }

    }

    private fun select(elem: View){
        val adapterPos = easyScrollPicker.getChildAdapterPosition(elem)
        if (adapterPos != lastSelectedPos){
            adapter.getItemAtPos(adapterPos)?.let { payload ->
                onItemSelect?.invoke(payload)
            }
            lastSelectedPos = adapterPos
        }

    }

    companion object{
        private const val VIEW_MAP_LIMIT = 100
    }
}