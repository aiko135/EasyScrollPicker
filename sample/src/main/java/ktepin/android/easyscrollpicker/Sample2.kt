package ktepin.android.easyscrollpicker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ktepin.android.easyscrollpicker.databinding.ActivitySample2Binding

class Sample2 : Activity() {
    private val binding: ActivitySample2Binding by lazy {
        ActivitySample2Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyScrollViewHolder<Int>(view) {
        val text: TextView

        init {
            text = view.findViewById(R.id.text)
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

        setContentView(binding.root)
    }
}