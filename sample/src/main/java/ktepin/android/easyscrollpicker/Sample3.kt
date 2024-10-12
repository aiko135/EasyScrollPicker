package ktepin.android.easyscrollpicker

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ktepin.android.easyscrollpicker.databinding.ActivitySample3Binding

class Sample3 : Activity() {
    private val binding: ActivitySample3Binding by lazy {
        ActivitySample3Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyScrollViewHolder<Int>(view) {
        val itemText: TextView
        val posText: TextView = view.findViewById(R.id.posText)

        override fun decorateViewAtPos(relativePos: Int, item: Int) {
            posText.text = String.format("%d", relativePos)
        }

        override fun buildAnimations(): List<EasyScrollAnimation> {
            val animator = ValueAnimator.ofFloat(14.0f, 16.0f).apply {
                addUpdateListener {
                    itemText.textSize = animatedValue as Float
                }
            }
            return listOf(EasyScrollAnimation(1,0,true, animator))
        }

        init {
            itemText = view.findViewById(R.id.payloadText)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // in generic pass <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val scrollPickerManager = EasyScrollManager<ItemViewHolder, Int>(
            easyScrollPicker = binding.easyScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item, parent, false)
                ItemViewHolder(view)
            },
            onBindViewHolder = { holder, item ->
                holder.text.text = item.toString()
            },
            onItemSelect = {
                Log.d("Test", "Selected $it")
            }
        )

        val dataset = (1..100).toList()
        scrollPickerManager.setInitialPosition(0)
        scrollPickerManager.setItems(dataset)


        setContentView(binding.root)
    }
}