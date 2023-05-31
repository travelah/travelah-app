package com.travelah.travelahapp.view.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.databinding.ActivityRegisterBinding
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        factory = ViewModelFactory.getInstance(this)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            backButton.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            registerButton.setOnClickListener {
                run {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)

                    if (edRegisterEmail.toString().isEmpty() || edRegisterPassword.toString().isEmpty() || edRegisterFullname.toString().isEmpty()) {
                        showFailedDialog(getString(R.string.invalid_input))
                    } else if (edRegisterEmail.error == null && edRegisterPassword.error == null && edRegisterFullname.error == null) {
                        registerViewModel
                            .postRegister(
                                edRegisterEmail.text.toString(),
                                edRegisterPassword.text.toString(),
                                edRegisterFullname.text.toString())
                            .observe(this@RegisterActivity) { result ->
                                if (result != null) {
                                    when (result) {
                                        is Result.Loading -> {
                                            showLoading(true)
                                        }
                                        is Result.Success -> {
                                            showLoading(false)
                                            AlertDialog.Builder(this@RegisterActivity).apply {
                                                setTitle(getString(R.string.success))
                                                setMessage((result.data))
                                                setPositiveButton(getString(R.string.next)) { _, _ ->
                                                    val intent = Intent(context, LoginActivity::class.java)
                                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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
                                                    R.string.register_failed_message
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                    }
                }
            }
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