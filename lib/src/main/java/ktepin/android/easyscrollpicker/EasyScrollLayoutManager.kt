package ktepin.android.easyscrollpicker

import android.util.Log
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
class EasyScrollLayoutManager<VH:EasyScrollViewHolder<I>, I>(
    private val easyScrollPicker: EasyScrollPicker,
    orientation: Int,
    reverseLayout: Boolean,
    private val selectDelay: Long,
    private val onItemSelect: ((item: I) -> Unit)?,
) : LinearLayoutManager(easyScrollPicker.context, orientation, reverseLayout) {
    private var prevCentralView: View? = null
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
        val containerMid = width / 2.0f
        val elemWidth = getChildAt(0)?.width
            ?: return //all containers have the same width, so we can take the width of the first one

        var i = 0
        while (i < childCount) {

            getChildAt(i)?.let { child ->
                // Calculating the distance between the child center and the center
                val childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f
                val distToCenter = abs(containerMid - childMid)

                if (distToCenter < elemWidth / 2) {
                    //central view detected here

                    if (child != prevCentralView){
                        notifyRelativePosChange(child, i, childCount)
                        prevCentralView = child
                    }
                }

                //make animation step
                animate(easyScrollPicker.getChildViewHolder(child)!! as VH, distToCenter, elemWidth)
            }

            i++
        }
    }

    private fun notifyRelativePosChange(central: View, centralViewIndex: Int, totalViews: Int){
        for (j in -centralViewIndex..-1)
            notifyView(getChildAt(centralViewIndex+j), j) //LEFT ITEMS

        notifyView(central, 0) //CENTRAL ITEM

        for (k in 1..totalViews)
            notifyView(getChildAt(centralViewIndex+k), k) //RIGHT ITEMS

        //notify central as selected
        onItemSelect?.let {
            selectWithDelay(central)
        }
    }

    private fun notifyView(v: View?, pos: Int){
        v?.let {
            easyScrollPicker.onItemChangeRelativePos<VH, I>(it, pos)
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

    private fun animate(vh: VH, distToCenter: Float, elemWidth: Int){
        val closerToCenterPosAbs = distToCenter.toInt() / elemWidth

        vh.animations.forEach { (animIndex, animator) ->
            if (animIndex == closerToCenterPosAbs){
                val mod = distToCenter % elemWidth
                var relativeFraction = mod / elemWidth
                relativeFraction = 1.0f - relativeFraction
                if (closerToCenterPosAbs == 1){
                    Log.d("test","Fraction $relativeFraction")
                }
                animator.setCurrentFraction(relativeFraction)
            }else{
                if (animIndex > closerToCenterPosAbs)
                    animator.setCurrentFraction(1.0f)
                else
                    animator.setCurrentFraction(0.0f)
            }
        }
    }
}