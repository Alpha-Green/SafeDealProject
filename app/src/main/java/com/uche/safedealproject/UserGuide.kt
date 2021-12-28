package com.uche.safedealproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.uche.safedealproject.databinding.ActivityUserGuideBinding

class UserGuide : AppCompatActivity() {
    private lateinit var binding: ActivityUserGuideBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sign.setOnClickListener{
            val intent = Intent(this,SignUpScreen::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
    }
}