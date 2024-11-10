package ktepin.android.easyscrollpicker.lib

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import ktepin.android.easyscrollpicker.lib.adapter.AbstractAdapter
import ktepin.android.easyscrollpicker.lib.adapter.HorizontalAdapter
import ktepin.android.easyscrollpicker.lib.adapter.VerticalAdapter
import ktepin.android.easyscrollpicker.lib.exception.ItemsOnScreenEvenException
import ktepin.android.easyscrollpicker.lib.exception.WrongAdapterException
import ktepin.android.easyscrollpicker.lib.exception.WrongLayoutManagerException

class EasyScrollPicker : AEasyScrollPicker {
    override var orientation = DEFAULT_ORIENTATION
    override var itemsOnScreen = DEFAULT_ITEMS_ON_SCREEN
    internal var initialPos = DEFAULT_INIT_POS
    private var selectDelay = DEFAULT_SELECT_DELAY_MS
    private var snapHelper: SnapHelper? = null
    private var scrollSpeedFactor = DEFAULT_SCROLL_SPEED_FACTOR

    init {
        this.clipToPadding = false
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let { applyAttributes(it) }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        attrs?.let { applyAttributes(it) }
    }

    private fun applyAttributes(attrSet: AttributeSet) {
        val attrs = context.obtainStyledAttributes(
            attrSet, R.styleable.EasyScrollPicker
        )

        val itemsOnScreen = attrs.getInt(R.styleable.EasyScrollPicker_itemsOnScreen, DEFAULT_ITEMS_ON_SCREEN)
        if (itemsOnScreen % 2 == 0)
            throw ItemsOnScreenEvenException(context)
        this.itemsOnScreen = itemsOnScreen

        val selectDelayAttr = attrs.getInt(R.styleable.EasyScrollPicker_selectDelayMs, DEFAULT_SELECT_DELAY_MS)
        if (selectDelayAttr >= 0)
            this.selectDelay = selectDelayAttr

        val enableSnap = attrs.getBoolean(R.styleable.EasyScrollPicker_enableSnapFinisher, DEFAULT_ENABLE_SNAP)
        if (enableSnap){
            snapHelper = LinearSnapHelper()
            snapHelper?.attachToRecyclerView(this)
        }

        val speedFactor = attrs.getFloat(R.styleable.EasyScrollPicker_scrollSpeedFactor, DEFAULT_SCROLL_SPEED_FACTOR)
        if (speedFactor > 0.0f)
            scrollSpeedFactor = speedFactor

        val orient = attrs.getInt(R.styleable.EasyScrollPicker_orientationMode, DEFAULT_ORIENTATION)
        orientation = if(orient == 1)
            LinearLayoutManager.VERTICAL
        else
            LinearLayoutManager.HORIZONTAL

        val initialPos = attrs.getInt(R.styleable.EasyScrollPicker_initialPosition, DEFAULT_INIT_POS)
        if (initialPos > 0)
            this.initialPos = initialPos

        attrs.recycle()
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is AbstractAdapter<*, *>) {
            super.setAdapter(adapter)
        } else {
            throw WrongAdapterException(context)
        }
    }

    override fun getAdapter(): AbstractAdapter<*, *>? = super.getAdapter() as AbstractAdapter<*, *>?

    override fun setLayoutManager(lm: LayoutManager?) {
        if (lm is EasyScrollLayoutManager<*, *>) {
            super.setLayoutManager(lm)
        } else {
            throw WrongLayoutManagerException(context)
        }
    }

    internal fun <VH : EasyScrollViewHolder<I>, I> configure(
        callbacks: EasyScrollCallbacks<VH, I>,
    ) {
        adapter = if (orientation == LinearLayoutManager.VERTICAL)
            VerticalAdapter(callbacks.onBindViewHolder, callbacks.onCreateViewHolder)
        else
            HorizontalAdapter(callbacks.onBindViewHolder, callbacks.onCreateViewHolder)

        layoutManager = EasyScrollLayoutManager<VH, I>(
            easyScrollPicker = this,
            orientation = orientation,
            reverseLayout = DEFAULT_REVERSE_LAYOUT,
            onItemSelect = callbacks.onItemSelect,
            selectDelay = selectDelay.toLong()
        )
    }

    internal fun <VH : EasyScrollViewHolder<I>, I> onItemChangeRelativePos(view: View, relativePos: Int){
        adapter?.getItemAtPos(getChildAdapterPosition(view))?.let { payloadItem ->
            val vh = getChildViewHolder(view) as VH
            payloadItem as I
            vh.decorateViewAtPos(relativePos, payloadItem)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if(orientation == LinearLayoutManager.VERTICAL){
            val requiredElemHeight = measuredHeight/ itemsOnScreen
            adapter?.applyElemSize(requiredElemHeight)
            val clipPadding: Int = measuredHeight / 2 - (requiredElemHeight / 2)
            setPadding(0, clipPadding, 0, clipPadding)
        }else{
            val requiredElemWidth = measuredWidth / itemsOnScreen
            adapter?.applyElemSize(requiredElemWidth)
            val clipPadding: Int = measuredWidth / 2 - (requiredElemWidth / 2)
            setPadding(clipPadding, 0, clipPadding, 0)
        }
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        if (scrollSpeedFactor > 0.0f){
            val modifiedX = (velocityX * scrollSpeedFactor).toInt()
            val modifiedY = (velocityY * scrollSpeedFactor).toInt()
            return super.fling(modifiedX, modifiedY)
        }else{
            return super.fling(velocityX, velocityY)
        }
    }

    internal fun setItems(items: List<*>) {
        if (initialPos > items.lastIndex)
            return //Index out of bounds

        post{
            adapter?.setItems(items as List<Nothing>)
            stopScroll()
            scrollToPosition(initialPos)
        }
    }

    companion object {
        private const val DEFAULT_ORIENTATION = LinearLayoutManager.HORIZONTAL
        private const val DEFAULT_REVERSE_LAYOUT = false
        private const val DEFAULT_SELECT_DELAY_MS = 0
        private const val DEFAULT_ENABLE_SNAP = false
        private const val DEFAULT_ITEMS_ON_SCREEN = 3
        private const val DEFAULT_INIT_POS = 0
        private const val DEFAULT_SCROLL_SPEED_FACTOR = -1.0f
    }
}