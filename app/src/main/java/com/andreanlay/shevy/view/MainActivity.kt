package com.andreanlay.shevy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.andreanlay.shevy.R
import com.andreanlay.shevy.databinding.ActivityMainBinding
import com.andreanlay.shevy.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = MainViewModel()

        title = "Shevy - Shape Recognition"
    }
}