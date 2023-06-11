package com.travelah.travelahapp.view.editprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.travelah.travelahapp.R
import com.travelah.travelahapp.databinding.ActivityEditProfileBinding
import com.travelah.travelahapp.view.main.MainActivity
import com.travelah.travelahapp.view.profile.ProfileFragment

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            buttonBack.setOnClickListener {
                onBackPressed()
            }
            buttonSaveChanges.setOnClickListener {
                val intent = Intent(this@EditProfileActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}