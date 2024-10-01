package ktepin.android.easyscrollpicker

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktepin.android.easyscrollpicker.exception.ItemsOnScreenEvenException
import ktepin.android.easyscrollpicker.exception.WrongAdapterException
import ktepin.android.easyscrollpicker.exception.WrongLayoutManagerException

class EasyScrollPicker : RecyclerView {

    private var callbacks: EasyScrollCallbacks<*, *>? = null

    private var numOfItemsOnScreen = DEFAULT_ITEMS_ON_SCREEN
    private var requiredElemWidth = 0
    internal var initialPos = DEFAULT_INIT_POS
    private var selectDelay = DEFAULT_SELECT_DELAY_MS

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
        this.numOfItemsOnScreen = itemsOnScreen

        val selectDelayAttr = attrs.getInt(R.styleable.EasyScrollPicker_selectDelayMs, DEFAULT_SELECT_DELAY_MS)
        if (selectDelayAttr >= 0)
            this.selectDelay = selectDelayAttr

        attrs.recycle()
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is EasyScrollAdapter<*, *>) {
            super.setAdapter(adapter)
        } else {
            throw WrongAdapterException(context)
        }
    }

    override fun getAdapter(): EasyScrollAdapter<*, *>? = super.getAdapter() as EasyScrollAdapter<*, *>?

    override fun setLayoutManager(lm: LayoutManager?) {
        if (lm is CustomLayoutManager<*>) {
            super.setLayoutManager(lm)
        } else {
            throw WrongLayoutManagerException(context)
        }
    }

    internal fun <VH : ViewHolder, I> configure(
        easyScrollCallbacks: EasyScrollCallbacks<VH, I>
    ) {
        callbacks = easyScrollCallbacks.also{
            adapter = EasyScrollAdapter(
                it.onCreateViewHolder,
                it.onBindViewHolder
            )
            layoutManager = CustomLayoutManager<I>(
                easyScrollPicker = this,
                orientation = DEFAULT_ORIENTATION,
                reverseLayout = DEFAULT_REVERSE_LAYOUT,
                onItemSelect = it.onItemSelect,
                selectDelay = selectDelay.toLong(),
                onItemChangeRelativePos =
                    it.decorateViewHolderAtPos?.let {{ untypedView, relativePos ->
                        easyScrollCallbacks.decorateViewHolderAtPos!!.invoke(
                            getChildViewHolder(untypedView) as VH,
                            relativePos
                        )
                    }}
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        requiredElemWidth = measuredWidth / numOfItemsOnScreen

        adapter?.let {
            it.elemWidthPx = requiredElemWidth
        }

        val clipPadding: Int = measuredWidth / 2 - (requiredElemWidth / 2)
        setPadding(clipPadding, 0, clipPadding, 0)
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
        private const val DEFAULT_ITEMS_ON_SCREEN = 3
        private const val DEFAULT_INIT_POS = 0
    }
}