package com.opera.ads.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.opera.ads.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot())
    }
}
