package ktepin.android.easyscrollpicker.sample

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import ktepin.android.easyscrollpicker.lib.EasyScrollManager
import ktepin.android.easyscrollpicker.lib.EasyScrollViewHolder
import ktepin.android.easyscrollpicker.sample.databinding.ActivitySample2Binding


class Sample2 : Activity() {
    private val binding: ActivitySample2Binding by lazy {
        ActivitySample2Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyScrollViewHolder<Int>(view) {
        val text: TextView = view.findViewById(R.id.text)
        val dot: ImageView = view.findViewById(R.id.dot)
        val stroke: FrameLayout = view.findViewById(R.id.stroke)

        override fun decorateViewAtPos(relativePos: Int, item: Int) {
           when(relativePos){
               0 ->  applyViewParams(R.color.primary, R.drawable.ic_dot, R.drawable.gradient_center)
               1 ->  applyViewParams(R.color.gray1, R.drawable.ic_dot_white, R.drawable.gradient_right)
               -1 -> applyViewParams(R.color.gray1, R.drawable.ic_dot_white, R.drawable.gradient_left)
               else ->  applyViewParams(R.color.gray2, R.drawable.ic_dot_gray, R.drawable.gradient_no)
           }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        private fun applyViewParams(colorId: Int, dotDrawable: Int, strokeDrawable: Int){
            text.context?.let {
                text.setTextColor(it.getColor(colorId))
                dot.setImageDrawable(it.getDrawable(dotDrawable))
                stroke.background = it.getDrawable(strokeDrawable)
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
                binding.selected.text  = "Selected $$it"
            }
        )

        val dataset = listOf(1, 5, 10 ,12, 15, 20, 25, 30, 50, 75, 100, 120, 150)
        scrollPickerManager.setInitialPosition(2)
        scrollPickerManager.setItems(dataset)

        window.statusBarColor = getColor(R.color.background_color)

        setContentView(binding.root)
    }
}