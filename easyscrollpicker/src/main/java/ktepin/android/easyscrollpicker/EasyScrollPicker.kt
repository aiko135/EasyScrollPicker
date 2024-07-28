package ktepin.android.easyscrollpicker

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktepin.android.easyscrollpicker.exception.ItemsOnScreenEvenException
import ktepin.android.easyscrollpicker.exception.WrongAdapterException
import ktepin.android.easyscrollpicker.exception.WrongLayoutManagerException

class EasyScrollPicker : RecyclerView {
    private var callbacks: EasyScrollCallbacks<*, *>? = null
    private var setItemsCallback: (() -> Unit)? = null

    private var numOfItemsOnScreen = DEFAULT_ITEMS_ON_SCREEN
    private var requiredElemWidth = 0
    internal var initialPos = 0

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

        val itemsOnScreen =
            attrs.getInt(R.styleable.EasyScrollPicker_itemsOnScreen, DEFAULT_ITEMS_ON_SCREEN)
        if (itemsOnScreen % 2 == 0)
            throw ItemsOnScreenEvenException(context)
        this.numOfItemsOnScreen = itemsOnScreen

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
                this,
                DEFAULT_ORIENTATION,
                DEFAULT_REVERSE_LAYOUT,
                it.onItemSelect
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

        adapter = adapter // fore trigger to redraw all elements of RV
        this.smoothScrollToPosition(0)

        doOnPreDraw {
            //postponed setItems operation
            setItemsCallback?.let { cb ->
                cb.invoke()
                setItemsCallback = null
            }
        }
    }

    internal fun setItems(items: List<*>) {
        val setItemsCb:()->Unit = {
            adapter?.setItems(items as List<Nothing>)
            applyInitPosition()
        }

        adapter?.let {
            setItemsCb.invoke()
        } ?: run {
            setItemsCallback = setItemsCb
        }
    }

    private fun applyInitPosition() {
        adapter?.let {
            var scrollPx = 0

            if (it.itemCount > 0) {
                if (initialPos <= (it.itemCount - 1)){
                    scrollPx = paddingStart + initialPos * requiredElemWidth
                }
            }

            stopScroll()
            scrollTo(scrollPx, 0)
        }
    }


    companion object {
        private val DEFAULT_ORIENTATION = LinearLayoutManager.HORIZONTAL
        private val DEFAULT_REVERSE_LAYOUT = false
        private val DEFAULT_ITEMS_ON_SCREEN = 3
    }
}