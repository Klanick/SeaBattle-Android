package com.example.seabattle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seabattle.api.SeaBattleService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = LoginFragment()
        fragment.arguments = Bundle().apply {
            //todo put data
        }
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .add(R.id.container, fragment, "loginFragment")
            .commit()
    }

}