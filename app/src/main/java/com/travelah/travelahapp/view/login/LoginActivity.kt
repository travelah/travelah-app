package com.travelah.travelahapp.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.travelah.travelahapp.R
import com.travelah.travelahapp.databinding.ActivityLoginBinding
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainActivity
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        factory = ViewModelFactory.getInstance(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            run {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)

                if (binding.edLoginEmail.text.toString()
                        .isEmpty() || binding.edLoginPassword.text.toString().isEmpty()
                ) {
                    showFailedDialog(getString(R.string.invalid_input))
                } else if (binding.edLoginEmail.error == null && binding.edLoginPassword.error == null) {
                    loginViewModel
                        .postLogin(
                            binding.edLoginEmail.text.toString(),
                            binding.edLoginPassword.text.toString()
                        )
                        .observe(this) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> {
                                        showLoading(true)
                                    }
                                    is Result.Success -> {
                                        showLoading(false)
                                        AlertDialog.Builder(this).apply {
                                            setTitle(getString(R.string.success))
                                            setMessage(getString(R.string.login_successful_message))
                                            setPositiveButton(getString(R.string.next)) { _, _ ->
                                                val intent =
                                                    Intent(context, MainActivity::class.java)
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                    is Result.Error -> {
                                        showFailedDialog(
                                            if (result.error != "") result.error else getString(
                                                R.string.login_failed_message
                                            )
                                        )
                                    }
                                }
                            }
                        }
                }
            }
        }
        binding.registerButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showFailedDialog(message: String) {
        showLoading(false)
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.failed))
            setMessage(message)
            setPositiveButton(getString(R.string.next)) { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}