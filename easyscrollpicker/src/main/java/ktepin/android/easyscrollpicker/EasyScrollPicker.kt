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
        var itemsOnScreen: Int = DEFAULT_ITEMS_ON_SCREEN
    )

    internal val attributes = Attributes()

    init {
        this.clipToPadding = false

        layoutManager = CustomLinearManager(context, DEFAULT_ORIENTATION, DEFAULT_REVERSE_LAYOUT)
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
            throw WrongAdapterException(context)
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

        val itemsOnScreen =
            attrs.getInt(R.styleable.EasyScrollPicker_itemsOnScreen, DEFAULT_ITEMS_ON_SCREEN)
        if (itemsOnScreen % 2 == 0)
            throw ItemsOnScreenEvenException(context)
        this.attributes.itemsOnScreen = itemsOnScreen

        attrs.recycle()
    }

    internal fun doAfterOnLayout(computedElemWidth: Int) {
        val clipPadding: Int = measuredWidth / 2 - (computedElemWidth / 2)
        setPadding(clipPadding, 0, clipPadding, 0)
    }

//    internal fun scrollToInitPosition(){
//        val adapter = adapter as EasyScrollAdapter<*, *>
//        if(adapter.itemCount > 0){
//           scrollTo(0, 0)
//        }
//    }

    companion object {
        private val DEFAULT_ORIENTATION = LinearLayoutManager.HORIZONTAL
        private val DEFAULT_REVERSE_LAYOUT = false
        private val DEFAULT_ITEMS_ON_SCREEN = 3
    }
}