package com.travelah.travelahapp.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.travelah.travelahapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
    }
}