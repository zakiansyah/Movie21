package com.dicoding.movie21.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dicoding.movie21.R
import com.dicoding.movie21.databinding.ActivitySplashScreenBinding
import com.dicoding.movie21.home.HomeActivity

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Handler().postDelayed( {
            startActivity(Intent(this, HomeActivity::class.java))
            this@SplashScreenActivity.overridePendingTransition(
                R.anim.slide_top,
                R.anim.slide_top
            )
            finish()
        }, 3000L)

    }
}