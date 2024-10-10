package ktepin.android.easyscrollpicker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import ktepin.android.easyscrollpicker.databinding.ActivitySample2Binding

class Sample2 : Activity() {
    private val binding: ActivitySample2Binding by lazy {
        ActivitySample2Binding.inflate(layoutInflater)
    }

    class ItemViewHolder(view: View) : EasyViewHolder<Int>(view) {
        val text: TextView

        init {
            text = view.findViewById(R.id.payloadText)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //wrong
        //binding.easyScrollPicker.adapter = Sample1Adapter()
        //--- TODO TEST IN FRAGMENT ---
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