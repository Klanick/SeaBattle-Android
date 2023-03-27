package com.example.seabattle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = MenuFragment()
        fragment.arguments = Bundle().apply {
            //todo put data
        }
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.container, fragment, "menuFragment")
            .commit()
    }
}