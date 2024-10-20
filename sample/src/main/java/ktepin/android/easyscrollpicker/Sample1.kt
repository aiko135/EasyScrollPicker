package ktepin.android.easyscrollpicker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.postDelayed
import ktepin.android.easyscrollpicker.databinding.ActivitySample1Binding

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
            easyScrollPicker = binding.easyScrollPicker,
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
            easyScrollPicker = binding.easyScrollPicker2,
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
        pickerManager2.setInitialPosition(4)
        binding.root.postDelayed(1000){
            pickerManager2.setItems(dataset)
        }


        setContentView(binding.root)
    }
}