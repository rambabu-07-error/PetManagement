package com.pawkeeperdev.activities.uploadPdf

import android.os.Bundle
import com.pawkeeperdev.activities.BaseActivity
import com.pawkeeperdev.databinding.ActivityUploadPdfBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadPdfActivity : BaseActivity() {
    private lateinit var binding: ActivityUploadPdfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        binding.btnUpload.setOnClickListener {

        }
    }

    private fun upload() {

    }

}