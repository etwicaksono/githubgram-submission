package com.etwicaksono.submission2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        setSupportActionBar(findViewById(R.id.toolbar))
        return super.onCreateOptionsMenu(menu)
    }
}