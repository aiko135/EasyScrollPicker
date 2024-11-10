package ktepin.android.easyscrollpicker.sample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ktepin.android.easyscrollpicker.lib.EasyScrollManager
import ktepin.android.easyscrollpicker.lib.EasyScrollViewHolder
import ktepin.android.easyscrollpicker.sample.databinding.ActivitySample1Binding


class Sample1 : Activity() {
    private val binding: ActivitySample1Binding by lazy {
        ActivitySample1Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyScrollViewHolder<Int>(view) {
        val payloadText: TextView = view.findViewById(R.id.payloadText)
        val posText: TextView = view.findViewById(R.id.posText)

        override fun decorateViewAtPos(relativePos: Int, item: Int) {
            posText.text = String.format("%d", relativePos)
            Log.d("Test", "holder $this at pos $relativePos with item $item")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // use in generic <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val pickerManager = EasyScrollManager<ItemViewHolder, Int>(
            easyScrollPicker = binding.firstScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item, parent, false)
                ItemViewHolder(view)
            },
            onBindViewHolder = { holder, item ->
                //Here we should bind holder with a passed item
                holder.payloadText.text = String.format("%d", item)
            },
            onItemSelect = {
                //here we got item (payload) of the central element
                binding.selected.text = it.toString()
            }
        )
        val dataset = (1..100).toList()
        pickerManager.setItems(dataset)


        val pickerManager2 = EasyScrollManager<ItemViewHolder, Int>(
            easyScrollPicker = binding.secondScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item, parent, false)
                ItemViewHolder(view)
            },
            onBindViewHolder = { holder, item ->
                holder.payloadText.text = item.toString()
            },
            onItemSelect = {
                binding.selected2.text = it.toString()
            }
        )
        pickerManager2.setItems(dataset)
        pickerManager2.setInitialPosition(4)

        setContentView(binding.root)
    }
}