package ktepin.android.easyscrollpicker

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktepin.android.easyscrollpicker.exception.WrongAdapterException

class EasyScrollPicker : RecyclerView {
    class Attributes(
        var itemsOnScreen: Int? = null
    )

    private val attributes = Attributes()

    init {
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

        val itemsOnScreen = attrs.getInt(R.styleable.EasyScrollPicker_itemsOnScreen, INT_NO_VALUE)
        this.attributes.itemsOnScreen = if (itemsOnScreen > 0) itemsOnScreen else null

        attrs.recycle()
    }

    companion object {
        private val DEFAULT_ORIENTATION = LinearLayoutManager.HORIZONTAL
        private val DEFAULT_REVERSE_LAYOUT = false
        private val INT_NO_VALUE = -1
    }
}