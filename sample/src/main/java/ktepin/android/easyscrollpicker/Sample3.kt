package ktepin.android.easyscrollpicker

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ktepin.android.easyscrollpicker.databinding.ActivitySample3Binding
import kotlin.math.abs

class Sample3 : Activity() {
    private val binding: ActivitySample3Binding by lazy {
        ActivitySample3Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyScrollViewHolder<Int>(view) {
        val itemText: TextView
        init {
            itemText = view.findViewById(R.id.payloadText)
            itemText.textSize = SMALL_TEXT_SIZE

            val animator = ValueAnimator.ofFloat(SMALL_TEXT_SIZE, BIG_TEXT_SIZE).apply {
                addUpdateListener {
                    itemText.textSize = animatedValue as Float
                    itemText.requestLayout()
                }
            }
            setAnimations(mapOf(0 to animator))
        }

        companion object{
            private const val BIG_TEXT_SIZE = 16.0f
            private const val SMALL_TEXT_SIZE = 10.0f
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