package ktepin.android.easyscrollpicker

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktepin.android.easyscrollpicker.exception.ItemsOnScreenEvenException
import ktepin.android.easyscrollpicker.exception.WrongAdapterException

class EasyScrollPicker : RecyclerView {
    class Attributes(
        var itemsOnScreen: Int? = null
    )

    internal val attributes = Attributes()

    init {
        this.clipToPadding = false
        //TODO maybe move to Manager
        layoutManager = PickerLinearManager(context, DEFAULT_ORIENTATION, DEFAULT_REVERSE_LAYOUT)
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

    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is EasyScrollAdapter<*, *>) {
            super.setAdapter(adapter)
        } else {
            throw WrongAdapterException(context.getString(R.string.easy_scroll_wrong_adapter))
        }
    }

    //TODO check what if user would use standart LinearLayoutManager
//    override fun setLayoutManager(lm: LayoutManager?) {
//        if (lm is PickerLinearManager){
//            super.setLayoutManager(lm)
//        }else{
//            throw WrongLayoutManagerException(context.getString(R.string.easy_scroll_wrong_adapter))
//        }
//    }

    private fun applyAttributes(attrSet: AttributeSet) {
        val attrs = context.obtainStyledAttributes(
            attrSet, R.styleable.EasyScrollPicker
        )

        val itemsOnScreenAttr = attrs.getInt(R.styleable.EasyScrollPicker_itemsOnScreen, INT_NO_VALUE)
        val itemsOnScreen = if (itemsOnScreenAttr > 0) itemsOnScreenAttr else null
        itemsOnScreen?.let {
            if(it % 2 == 0)
                throw ItemsOnScreenEvenException(context.getString(R.string.easy_scroll_items_on_screen_even))
        }
        this.attributes.itemsOnScreen = itemsOnScreen


        attrs.recycle()
    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)

        if (childCount < 1)
            return

        val firstChildWidth = getChildAt(0).measuredWidth
        val lastItemWidth = getChildAt(childCount - 1).measuredWidth

        val paddingLeft: Int = measuredWidth / 2 - (firstChildWidth / 2)
        val paddingRight: Int = measuredWidth / 2 - (lastItemWidth / 2)
        setPadding(paddingLeft, 0, paddingRight, 0)
    }

    companion object {
        private val DEFAULT_ORIENTATION = LinearLayoutManager.HORIZONTAL
        private val DEFAULT_REVERSE_LAYOUT = false
        private val INT_NO_VALUE = -1
    }
}