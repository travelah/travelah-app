package com.travelah.travelahapp.view.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.travelah.travelahapp.databinding.ActivitySplashScreenBinding
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.login.LoginActivity
import com.travelah.travelahapp.view.main.MainActivity
import com.travelah.travelahapp.view.register.RegisterViewModel

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var factory: ViewModelFactory
    private val splashScreenViewModel: SplashScreenViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = ViewModelFactory.getInstance(this)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        var tokenIsEmpty = true
        splashScreenViewModel.token.observe(this) { token ->
            if (!token.isNullOrEmpty()) {
                tokenIsEmpty = false
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (tokenIsEmpty) {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)

    }
}