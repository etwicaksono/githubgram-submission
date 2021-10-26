package com.etwicaksono.githubgram

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.etwicaksono.githubgram.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

    }
}