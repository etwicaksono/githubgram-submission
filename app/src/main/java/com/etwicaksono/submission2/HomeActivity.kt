package com.etwicaksono.submission2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.etwicaksono.submission2.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

    }
}