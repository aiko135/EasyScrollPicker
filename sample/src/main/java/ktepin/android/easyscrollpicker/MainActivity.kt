package ktepin.android.easyscrollpicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ktepin.android.easyscrollpicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnSample1.setOnClickListener {
            startActivity(Intent(this, Sample1::class.java))
        }
        binding.btnSample2.setOnClickListener {
            startActivity(Intent(this, Sample2::class.java))
        }
        setContentView(binding.root)
    }


}