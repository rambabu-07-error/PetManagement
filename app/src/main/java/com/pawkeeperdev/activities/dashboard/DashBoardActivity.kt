package com.pawkeeperdev.activities.dashboard

import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.pawkeeperdev.activities.BaseActivity
import com.pawkeeperdev.activities.dashboard.ui.home.HomeFragment
import com.pawkeeperdev.activities.dashboard.ui.profile.ProfileFragment
import com.pawkeeperdev.config.CommonUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pawkeeperdev.R
import com.pawkeeperdev.config.applyBottomAndSideInsets
import com.pawkeeperdev.config.applyTopAndSideInsets
import com.pawkeeperdev.databinding.ActivityDashBoardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : BaseActivity() {
    companion object {
        const val KEY_EXTRA = "extraData"
    }

    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var container: FrameLayout
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.container.applyTopAndSideInsets()
        binding.bottomNav.applyBottomAndSideInsets()
        CommonUtils.thisIsDashBoard = true
        container = binding.container
        bottomNav = binding.bottomNav
        loadFrag(HomeFragment())
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                bottomNav.selectedItemId -> {
                    true
                }
                R.id.home -> {
                    loadFrag(HomeFragment())
                    true
                }
                R.id.profile -> {
                    loadFrag(ProfileFragment())
                    true
                }
                else -> {
                    loadFrag(HomeFragment())
                    true
                }
            }
        }

    }

    private fun loadFrag(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.container.id, fragment)
        transaction.commit()
    }

    override fun onResume() {
        CommonUtils.thisIsDashBoard = true
        super.onResume()
    }
}