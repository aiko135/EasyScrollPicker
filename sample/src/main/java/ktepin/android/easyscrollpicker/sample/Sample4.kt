package ktepin.android.easyscrollpicker.sample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.postDelayed
import ktepin.android.easyscrollpicker.lib.EasyScrollManager
import ktepin.android.easyscrollpicker.lib.EasyScrollViewHolder
import ktepin.android.easyscrollpicker.sample.databinding.ActivitySample4Binding

class Sample4 : Activity() {
    private val binding: ActivitySample4Binding by lazy {
        ActivitySample4Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyScrollViewHolder<Int>(view) {
        val payloadText: TextView = view.findViewById(R.id.payloadText)

//        override fun decorateViewAtPos(relativePos: Int, item: Int) {
//
//            Log.d("Test", "holder $this at pos $relativePos with item $item")
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // use in generic <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val pickerManager = EasyScrollManager<ItemViewHolder, Int>(
            easyScrollPicker = binding.easyScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item3, parent, false)
                ItemViewHolder(view)
            },
            onBindViewHolder = { holder, item ->
                //Here we should bind holder with a passed item
                holder.payloadText.text = String.format("%d", item)
            },
            onItemSelect = {
                //here we got item (payload) of the central element
//                binding.selected.text = it.toString()
            }
        )
        val dataset = (1..100).toList()
        pickerManager.setItems(dataset)

        setContentView(binding.root)
    }
}