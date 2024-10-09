package ktepin.android.easyscrollpicker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktepin.android.easyscrollpicker.databinding.ActivitySample1Binding

class Sample1 : Activity() {
    private val binding: ActivitySample1Binding by lazy {
        ActivitySample1Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val payloadText: TextView = view.findViewById(R.id.payloadText)
        val posText: TextView = view.findViewById(R.id.posText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // use in generic <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val scrollPickerManager = EasyScrollManager<ItemViewHolder, Int>(
            easyScrollPicker = binding.easyScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_item, parent, false)
                ItemViewHolder(view)
            },
            onBindViewHolder = { holder, item ->
                holder.payloadText.text = item.toString()
            },
            onItemSelect = {
                binding.selected.text = it.toString()
            },
            decorateViewHolderAtPos = { holder, relativePos, item ->
                holder.posText.text = relativePos.toString()
                Log.d("Test", "holder $holder at pos $relativePos with item $item")
            }
        )

        val dataset = (1..100).toList()
        scrollPickerManager.setInitialPosition(0)
        scrollPickerManager.setItems(dataset)

//        GlobalScope.launch {
//            delay(5000)
//            runOnUiThread {
//                scrollPickerManager.setInitialPosition(4)
//                scrollPickerManager.setItems(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14 ,15, 16, 17))
//            }
//        }

        setContentView(binding.root)
    }
}