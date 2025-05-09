package com.pawkeeperdev.activities.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.pawkeeperdev.activities.BaseActivity
import com.pawkeeperdev.activities.signup.SignUpActivity
import com.pawkeeperdev.config.CommonUtils
import com.pawkeeperdev.config.TouchEffectListener
import com.pawkeeperdev.activities.dashboard.DashBoardActivity
import com.pawkeeperdev.databinding.ActivitySignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val utcTime = "2024-09-24 12:21:06"
        CommonUtils.addLogE("TimerDemo" , "UtcTime to Local Hour minute ${CommonUtils.calculateTimeDifference(utcTime)}")

        binding.tvBtnSignUp.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
        }

        binding.edTvPassWord.setOnEditorActionListener { _, actionId, _ ->
            var bool = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                bool = true
                logIn()
            }
            return@setOnEditorActionListener bool
        }

        binding.btnLogin.setOnTouchListener(TouchEffectListener())
        binding.btnLogin.setOnClickListener {
            logIn()
        }
        viewModel.signInResult.observe(this) { result ->
            result.fold(onSuccess = { user ->
                CommonUtils.showCustomToast(binding.root, "User Log in Successful!", true)
                startActivity(
                    Intent(this@SignInActivity, DashBoardActivity::class.java).putExtra(
                        DashBoardActivity.KEY_EXTRA, user.id
                    )
                )
                finish()
            }, onFailure = { exception ->
                CommonUtils.showCustomToast(
                    binding.root,
                    exception.message ?: "An error occurred!",
                    false
                )
            })
        }
    }

    private fun logIn() {
        val userEmail = binding.edTvEmail.text.toString().trim()
        val password = binding.edTvPassWord.text.toString().trim()

        if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.edTvEmail.requestFocus()
            CommonUtils.showCustomToast(binding.root, "Enter email", false)
            return
        } else if (TextUtils.isEmpty(password)) {
            binding.edTvPassWord.requestFocus()
            CommonUtils.showCustomToast(binding.root, "Enter password", false)
            return
        } else {
            viewModel.signInUser(email = userEmail, password = password)
        }

    }
}