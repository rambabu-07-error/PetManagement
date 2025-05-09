package com.pawkeeperdev.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pawkeeperdev.config.CommonUtils
import com.pawkeeperdev.R

open class BaseActivity : AppCompatActivity() {

   companion object val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (CommonUtils.thisIsDashBoard) {
                    CommonUtils.thisIsDashBoard = false
                    finishAffinity()
                } else {
                    finish()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.deepBlue)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        CommonUtils.addLogE("onBackPressedCallback"," bool : ${CommonUtils.thisIsDashBoard}")
    }


    override fun onDestroy() {
        onBackPressedCallback.remove()
        super.onDestroy()
    }
}