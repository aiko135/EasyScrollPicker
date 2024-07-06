package ktepin.android.easyscrollpicker

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktepin.android.easyscrollpicker.databinding.ActivitySample1Binding

class Sample1 : Activity() {
    private val binding: ActivitySample1Binding by lazy {
        ActivitySample1Binding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //wrong
        //binding.easyScrollPicker.adapter = Sample1Adapter()

        // in generic pass <YOUR_CUSTOM_VIEW_HOLDER, PAYLOAD_TYPE>
        val scrollConfig = EasyScrollManager<Sample1Adapter.SelectorViewHolder, Int>(
            easyScrollPicker = binding.easyScrollPicker,
            onCreateViewHolder = { parent ->
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_item_number, parent, false)
                Sample1Adapter.SelectorViewHolder(view)
            },
            onBindViewHolder = {holder, item ->
                holder.text.text = item.toString()
            }
        )

        scrollConfig.setItems(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14 ,15, 16, 17))

//        GlobalScope.launch {
//            delay(1000)
//            runOnUiThread {
//                scrollConfig.setItems(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14 ,15, 16, 17))
//            }
//        }

        setContentView(binding.root)
    }
}