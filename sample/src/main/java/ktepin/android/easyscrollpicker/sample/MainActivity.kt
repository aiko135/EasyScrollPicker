package ktepin.android.easyscrollpicker.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ktepin.android.easyscrollpicker.sample.databinding.ActivityMainBinding

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
        binding.btnSample3.setOnClickListener {
            startActivity(Intent(this, Sample3::class.java))
        }
        binding.btnSample4.setOnClickListener {
            startActivity(Intent(this, Sample4::class.java))
        }
        setContentView(binding.root)
    }


}