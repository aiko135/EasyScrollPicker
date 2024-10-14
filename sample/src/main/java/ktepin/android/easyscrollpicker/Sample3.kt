package ktepin.android.easyscrollpicker

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ktepin.android.easyscrollpicker.databinding.ActivitySample3Binding

class Sample3 : Activity() {
    private val binding: ActivitySample3Binding by lazy {
        ActivitySample3Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyScrollViewHolder<Int>(view) {
        private val bigTextSize = dpToPx(view.context, 24)
        private val smallTextSize = dpToPx(view.context, 18)

        val itemText: TextView

        init {
            itemText = view.findViewById(R.id.payloadText)
            itemText.setTextSize(COMPLEX_UNIT_PX, smallTextSize)

            val animator = ValueAnimator.ofFloat(smallTextSize, bigTextSize).apply {
                addUpdateListener {
                    itemText.setTextSize(COMPLEX_UNIT_PX, animatedValue as Float)
                    itemText.invalidate()
                    itemText.parent.requestLayout()
                }
            }
            setAnimations(mapOf(0 to animator))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // in generic pass <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val scrollPickerManager = EasyScrollManager<ItemViewHolder, Int>(
            easyScrollPicker = binding.easyScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item3, parent, false)
                ItemViewHolder(view)
            },
            onBindViewHolder = { holder, item ->
                holder.itemText.text = item.toString()
            },
            onItemSelect = {
                Log.d("Test", "Selected $it")
            }
        )

        val dataset = (1..100).toList()
        scrollPickerManager.setItems(dataset)
        scrollPickerManager.setInitialPosition(1)

        setContentView(binding.root)
    }
}