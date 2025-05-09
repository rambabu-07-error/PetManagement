package com.pawkeeperdev.activities.addPets

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.pawkeeperdev.config.CommonUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPetActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA_PET="extraPets"
        const val EXTRA_WHERE="whereFrom"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialog = AddPetsDialogFragment()
        dialog.show(supportFragmentManager, "MyCustomDialog")
        dialog.setCancelable(false)
        setFinishOnTouchOutside(false)
    }

    override fun onDestroy() {
        CommonUtils.thisIsDashBoard = true
        super.onDestroy()
    }
}