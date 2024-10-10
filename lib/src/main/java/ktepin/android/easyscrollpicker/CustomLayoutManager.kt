package ktepin.android.easyscrollpicker

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs


/**
 * inspired by
 * https://github.com/adityagohad/HorizontalPicker/blob/master/horizontalpickerlib/src/main/java/travel/ithaka/android/horizontalpickerlib/PickerLayoutManager.java
 */
class CustomLayoutManager<VH:EasyViewHolder<I>, I>(
    private val easyScrollPicker: EasyScrollPicker,
    orientation: Int,
    reverseLayout: Boolean,
    private val selectDelay: Long,
    private val onItemSelect: ((item: I) -> Unit)?,
) : LinearLayoutManager(easyScrollPicker.context, orientation, reverseLayout) {
    private val viewMap = mutableMapOf<View, Int>()
    private var waitJob: Job? = null
    private val adapter = easyScrollPicker.adapter as EasyScrollAdapter<*,I>
    private var lastSelectedPos = -1

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        observeOnChange()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?,
    ): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
            observeOnChange()
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

                    for (j in -i..-1)
                        checkIfItemPosChanged(getChildAt(i+j), j) //LEFT ITEMS

                    checkIfItemPosChanged(child, 0) //CENTRAL ITEM

                    for (k in 1..childCount)
                        checkIfItemPosChanged(getChildAt(i+k), k) //RIGHT ITEMS

                    //Select the central elem
                    onItemSelect?.let {
                        selectWithDelay(child)
                    }

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
                    easyScrollPicker.onItemChangeRelativePos<VH, I>(view, relativePos)
                }
            } ?:run {
                viewMap[v] = relativePos
                easyScrollPicker.onItemChangeRelativePos<VH, I>(view, relativePos)
            }
        }

    }

    private fun selectWithDelay(view:View){
        if (selectDelay == 0L){
            select(view)
            return
        }

        var delay:Long = selectDelay

        if (waitJob != null){
            waitJob!!.cancel()
            waitJob = null
        }else{
            delay = 0
        }

        waitJob = GlobalScope.launch(Dispatchers.IO) {
            delay(delay)
            withContext(Dispatchers.Main){
                select(view)
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