package ktepin.android.easyscrollpicker

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ktepin.android.easyscrollpicker.exception.WrongAdapterException
import ktepin.android.easyscrollpicker.exception.WrongLayoutManagerException

class EasyScrollPicker : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    init {
        layoutManager = PickerLinearManager(context, DEFAULT_ORIENTATION, DEFAULT_REVERSE_LAYOUT)
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

    companion object {
        private val DEFAULT_ORIENTATION = LinearLayoutManager.HORIZONTAL
        private val DEFAULT_REVERSE_LAYOUT = false
    }
}