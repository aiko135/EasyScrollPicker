package ktepin.android.easyscrollpicker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktepin.android.easyscrollpicker.databinding.ActivitySample1Binding

class Sample1 : Activity() {
    private val binding: ActivitySample1Binding by lazy {
        ActivitySample1Binding.inflate(layoutInflater)
    }

    class SelectorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView

        init {
            text = view.findViewById(R.id.text)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //wrong
        //binding.easyScrollPicker.adapter = Sample1Adapter()

        // in generic pass <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val scrollPickerManager = EasyScrollManager<SelectorViewHolder, Int>(
            easyScrollPicker = binding.easyScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_number, parent, false)
                SelectorViewHolder(view)
            },
            onBindViewHolder = { holder, item ->
                holder.text.text = item.toString()
            },
            onItemSelected = {
                Log.d("test", "Selected $it")
            }
        )

        val dataset = (1..100).toList()
        scrollPickerManager.setItems(dataset)

//        GlobalScope.launch {
//            delay(5000)
//            runOnUiThread {
//                scrollPickerManager.setItems(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14 ,15, 16, 17))
//            }
//        }

        setContentView(binding.root)
    }
}