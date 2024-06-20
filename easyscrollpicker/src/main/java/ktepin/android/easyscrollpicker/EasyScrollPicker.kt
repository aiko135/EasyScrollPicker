package ktepin.android.easyscrollpicker

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EasyScrollPicker : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutManager = PickerLinearManager(context, DEFAULT_ORIENTATION, DEFAULT_REVERSE_LAYOUT)
    }

    companion object {
        private val DEFAULT_ORIENTATION = LinearLayoutManager.HORIZONTAL
        private val DEFAULT_REVERSE_LAYOUT = false
    }
}