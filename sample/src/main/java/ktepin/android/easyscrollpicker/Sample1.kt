package ktepin.android.easyscrollpicker

import android.app.Activity
import android.os.Bundle
import ktepin.android.easyscrollpicker.databinding.ActivityMainBinding
import ktepin.android.easyscrollpicker.databinding.ActivitySample1Binding

class Sample1 : Activity() {
    private val binding: ActivitySample1Binding by lazy {
        ActivitySample1Binding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.easyScrollPicker.adapter = Sample1Adapter()
        setContentView(binding.root)
    }
}