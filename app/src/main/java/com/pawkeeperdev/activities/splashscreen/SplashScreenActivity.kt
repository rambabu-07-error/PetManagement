package com.pawkeeperdev.activities.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pawkeeperdev.R
import com.pawkeeperdev.activities.dashboard.DashBoardActivity
import com.pawkeeperdev.activities.signIn.SignInActivity
import com.pawkeeperdev.databinding.ActivitySplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: SplashViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val tvLabel = findViewById<TextView>(R.id.tvLabel)
        val animation = AnimationUtils.loadAnimation(this, R.anim.foldout)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                viewModel.getWhoAlreadyLogIn()
            }
        })

        tvLabel.startAnimation(animation)



        viewModel.userData.observe(this) { user ->
            run {
                binding.root.postDelayed({
                    if (user != null) {
                        startActivity(
                            Intent(
                                this@SplashScreenActivity,
                                DashBoardActivity::class.java
                            ).putExtra(DashBoardActivity.KEY_EXTRA, user.id)
                        )
                        finish()
                    } else {
                        startActivity(
                            Intent(this@SplashScreenActivity, SignInActivity::class.java)
                        )
                        finish()
                    }
                }, 2000)
            }
        }
    }
}