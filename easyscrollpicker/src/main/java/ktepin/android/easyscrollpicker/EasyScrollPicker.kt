package ktepin.android.easyscrollpicker

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktepin.android.easyscrollpicker.exception.ItemsOnScreenEvenException
import ktepin.android.easyscrollpicker.exception.WrongAdapterException
import ktepin.android.easyscrollpicker.exception.WrongLayoutManagerException

class EasyScrollPicker : RecyclerView {
    class Attributes(
        var itemsOnScreen: Int = DEFAULT_ITEMS_ON_SCREEN
    )

    internal var requiredElemWidth = 0
    private val attributes = Attributes()
    private var initialPos = 0

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
        this.attributes.itemsOnScreen = itemsOnScreen

        attrs.recycle()
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is EasyScrollAdapter<*, *>) {
            super.setAdapter(adapter)
        } else {
            throw WrongAdapterException(context)
        }
    }

    override fun getAdapter(): EasyScrollAdapter<*, *> = super.getAdapter() as EasyScrollAdapter<*, *>

    override fun setLayoutManager(lm: LayoutManager?) {
        if (lm is CustomLayoutManager<*>) {
            super.setLayoutManager(lm)
        } else {
            throw WrongLayoutManagerException(context)
        }
    }

    internal fun <VH : ViewHolder, I> init(
        onCreateViewHolder: (parent: ViewGroup) -> VH,
        onBindViewHolder: (holder: VH, item: I) -> Unit,
        onItemSelect: ((item: I) -> Unit)?
    ) {
        adapter = EasyScrollAdapter(
            onCreateViewHolder,
            onBindViewHolder
        )
        layoutManager = CustomLayoutManager<I>(
            this,
            DEFAULT_ORIENTATION,
            DEFAULT_REVERSE_LAYOUT,
            onItemSelect
        )
    }

    internal fun applyInitPosition() {
        if (adapter.itemCount > 0) {
            stopScroll()
            scrollTo(0, 0)
        }
    }

    internal fun measureElemWidth() {
        requiredElemWidth = attributes.itemsOnScreen.let {
            this.measuredWidth / it
        }
    }

    internal fun measureAndApplyPadding() {
        val clipPadding: Int = measuredWidth / 2 - (requiredElemWidth / 2)
        setPadding(clipPadding, 0, clipPadding, 0)
    }

    companion object {
        private val DEFAULT_ORIENTATION = LinearLayoutManager.HORIZONTAL
        private val DEFAULT_REVERSE_LAYOUT = false
        private val DEFAULT_ITEMS_ON_SCREEN = 3
    }
}