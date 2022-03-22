package com.example.luconnect

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val name = intent.getStringExtra("name")

        val text:TextView = findViewById(R.id.visit_user_name);

        text.text = name
    }
}