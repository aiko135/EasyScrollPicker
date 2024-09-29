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
    private val onItemSelect: ((item:I)->Unit)?
) : LinearLayoutManager(easyScrollPicker.context, orientation, reverseLayout) {

    private val adapter = easyScrollPicker.adapter as EasyScrollAdapter<*,I>
    private var lastSelectedPos = -1

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        onChange()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            onChange()
            scrolled
        } else {
            0
        }
    }

    private fun onChange() {
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

//                    applyVisual(child, ElemPos.CENTRAL)
//                    getChildAt(i - 1)?.let { applyVisual(it, ElemPos.LEFT_NEIGHBOR) }
//                    getChildAt(i + 1)?.let { applyVisual(it, ElemPos.RIGHT_NEIGHBOR) }
                    i++ //скипаем i+1 элемент
                } else {
//                    applyVisual(child, ElemPos.REST)
                }
            }
            i++
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
}