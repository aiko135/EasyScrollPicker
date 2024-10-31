package ktepin.android.easyscrollpicker

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ktepin.android.easyscrollpicker.databinding.ActivitySample2Binding
import kotlin.math.abs

class Sample2 : Activity() {
    private val binding: ActivitySample2Binding by lazy {
        ActivitySample2Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyScrollViewHolder<Int>(view) {
        val text: TextView = view.findViewById(R.id.text)
        val dot: ImageView = view.findViewById(R.id.dot)

        override fun decorateViewAtPos(relativePos: Int, item: Int) {
           when(abs(relativePos)){
               0 -> applyColor(R.color.primary)
               1 -> applyColor(R.color.gray1)
               else -> applyColor(R.color.gray2)
           }
        }

        private fun applyColor(colorId: Int){
            text.context?.let {
                text.setTextColor(it.getColor(colorId))
                dot.setBackgroundColor(colorId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // in generic pass <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val scrollPickerManager = EasyScrollManager<ItemViewHolder, Int>(
            easyScrollPicker = binding.easyScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item2, parent, false)
                ItemViewHolder(view)
            },
            onBindViewHolder = { holder, item ->
                holder.text.text = "$${item}";
            },
            onItemSelect = {
                Log.d("Test", "Selected $it")
            }
        )

        val dataset = listOf(1, 5, 10 ,12, 15, 20, 25, 30, 50, 75, 100, 120, 150)
        scrollPickerManager.setInitialPosition(2)
        scrollPickerManager.setItems(dataset)

        window.statusBarColor = getColor(R.color.background_color)

        setContentView(binding.root)
    }
}