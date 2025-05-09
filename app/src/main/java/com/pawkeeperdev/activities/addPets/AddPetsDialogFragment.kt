package com.pawkeeperdev.activities.addPets

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pawkeeperdev.activities.dashboard.DashBoardActivity
import com.pawkeeperdev.adapters.PetIconAdapter
import com.pawkeeperdev.config.CommonUtils
import com.pawkeeperdev.config.TouchEffectListener
import com.pawkeeperdev.entities.PetDataModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.pawkeeperdev.R
import com.pawkeeperdev.databinding.FragmentAddPetsDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class AddPetsDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddPetsDialogBinding
    private val viewModel: AddPetViewModel by viewModels()
    private lateinit var petAdapter: PetIconAdapter
    private var id: Long = 0
    private var fromwhere: String = ""
    private var petType: String = ""
    private var mUri: Uri? = null
    private val cal: Calendar = Calendar.getInstance()
    private val launcher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri == null) {
                CommonUtils.showCustomToast(binding.root, "Image not selected!", false)
            } else {
                mUri = uri
                Glide.with(requireContext()).load(uri)
                    .placeholder(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_add_pets))
                    .into(binding.ivPetImage)
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPetsDialogBinding.inflate(inflater, container, false)


        binding.btnAddPet.setOnTouchListener(TouchEffectListener())
        binding.btnOk.setOnTouchListener(TouchEffectListener())
        binding.btnClose.setOnTouchListener(TouchEffectListener())

        if (activity?.intent?.hasExtra(AddPetActivity.EXTRA_WHERE) != null) {
            fromwhere = activity?.intent?.getStringExtra(AddPetActivity.EXTRA_WHERE).toString()
        }

        if (activity?.intent?.getLongExtra(AddPetActivity.EXTRA_DATA_PET, 0) != null) {
            id = activity?.intent?.getLongExtra(AddPetActivity.EXTRA_DATA_PET, 0)!!
        } else {
            binding.mainLayout.visibility = View.GONE
        }


        binding.btnSkip.setOnClickListener {
            if (fromwhere == "fromSignUp") {
                val intent = Intent(requireContext(), DashBoardActivity::class.java)
                intent.putExtra(DashBoardActivity.KEY_EXTRA, id)
                requireContext().startActivity(intent)
                requireActivity().finish()
            }
        }
        binding.btnImgCancel.setOnClickListener {
            if (activity != null)
                activity?.finish()
        }
        binding.parentInIv.setOnClickListener {
            checkPermission()
        }

        binding.recyclerPetDummy.layoutManager = GridLayoutManager(requireContext(), 4)
        petAdapter = PetIconAdapter(
            requireContext(), CommonUtils.getIconList(),
            object : PetIconAdapter.OnItemClick {
                override fun onClickIcon(position: Int) {
                    val iconResId = CommonUtils.getIconList()[position]
                    Glide.with(binding.root)
                        .asBitmap()
                        .load(iconResId)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                binding.ivPetImage.setImageBitmap(resource)
                                mUri = CommonUtils.bitmapToUri(requireContext(), resource)
                                CommonUtils.addLogE(
                                    "AddPetsDialogFragment",
                                    "listener Selected URI: $mUri"
                                )
                                binding.parentOutIV.borderColor =
                                    requireContext().getColor(R.color.color_primary_dark)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                }
            })
        binding.recyclerPetDummy.adapter = petAdapter

        visibleLayout()


        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.tvBtnDob.text = CommonUtils.changeDateFormat(" dd : MMM : YYYY ", cal.time)
            }

        binding.tvBtnDob.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }


        val petTypes = listOf("Select Pet Type", "Cat", "Dog", "others")
        val adapter1 = ArrayAdapter(requireContext(), R.layout.item_spinner_pet_type, petTypes)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPetType.adapter = adapter1

        binding.spinnerPetType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (val selectedPetType = petTypes[position]) {
                        "others" -> {
                            binding.cardCustomPetType.visibility = View.VISIBLE
                            petType = ""
                        }

                        "Select Pet Type" -> {
                            binding.cardCustomPetType.visibility = View.GONE
                            binding.llOptionBtn.visibility = View.GONE
                            petType = ""
                        }

                        else -> {
                            binding.cardCustomPetType.visibility = View.GONE
                            binding.llOptionBtn.visibility = View.GONE
                            binding.edTvPetType.setText("")
                            petType = selectedPetType
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.edTvPetType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    binding.llOptionBtn.visibility = View.VISIBLE
                    petType = s.toString()
                } else {
                    binding.llOptionBtn.visibility = View.GONE
                }
            }
        })

        binding.btnOk.setOnClickListener {
            petType = binding.edTvPetType.text.toString()
            CommonUtils.hideKeyboard(binding.root)
        }
        binding.btnClose.setOnClickListener {
            binding.edTvPetType.setText("")
            binding.llOptionBtn.visibility = View.GONE
        }

        binding.btnAddPet.setOnClickListener {
            addPet()
        }

        viewModel.addPetResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { message ->
                    CommonUtils.showCustomToast(binding.root, message, true)
                    val intent = Intent(requireContext(), DashBoardActivity::class.java)
                    intent.putExtra(DashBoardActivity.KEY_EXTRA, id)
                    requireContext().startActivity(intent)
                },
                onFailure = { exception ->
                    binding.progress.visibility = View.GONE
                    binding.btnAddPet.visibility = View.VISIBLE
                    CommonUtils.showCustomToast(binding.root, exception.message, true)
                })
        }

        return binding.root
    }


    private fun addPet() {
        if (id != 0L) {
            if (mUri == null) {
                CommonUtils.showCustomToast(binding.root, "Select an Icon For Pet!", false)
                return
            } else if (binding.edTvPetName.text.isEmpty()) {
                CommonUtils.showCustomToast(binding.root, "Enter Pet Name", false)
                return
            } else if (petType == "") {
                CommonUtils.showCustomToast(binding.root, "Select pet Type", false)
                return
            } else if (binding.tvBtnDob.text == requireContext().getString(R.string.str_dd_mm_yyyy)) {
                CommonUtils.showCustomToast(binding.root, "Select DOB/DOA", false)
                return
            } else {
                binding.progress.visibility = View.VISIBLE
                binding.btnAddPet.visibility = View.INVISIBLE
                val byteArray = CommonUtils.resizeAndConvertToByteArray(mUri!!, requireContext())
                val petDat = PetDataModel(
                    ownerId = id,
                    petName = binding.edTvPetName.text.toString(),
                    petType = petType,
                    petAdoptDate = binding.tvBtnDob.text.toString(),
                    petImage = byteArray
                )
                viewModel.addPet(petDat)
            }
        }

    }


    private fun visibleLayout() {
        binding.btnSkip.visibility = View.GONE
        binding.btnImgCancel.visibility = View.GONE

        if (id != 0L) {
            binding.mainLayout.visibility = View.VISIBLE
        } else {
            binding.mainLayout.visibility = View.GONE
        }

        if (fromwhere == "home") {
            binding.btnImgCancel.visibility = View.VISIBLE
        } else if (fromwhere == "fromSignUp") {
            binding.btnSkip.visibility = View.VISIBLE
        } else {
            binding.btnImgCancel.visibility = View.VISIBLE
        }
    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach { (permission, isGranted) ->
                if (permission == READ_MEDIA_IMAGES) {
                    if (isGranted) {
                        showCameraDialog()
                    } else {
                        goToSetting()
                    }
                } else {
                    if (isGranted) {
                        showCameraDialog()
                    } else {
                        goToSetting()
                    }
                }
            }
        }


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }

    private fun goToSetting() {
        CommonUtils.showCustomToast(binding.root, "Permission denied", false)
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    private fun showCameraDialog() {
        CommonUtils.showCameraDialog(
            requireContext(),
            btnCameraClick = {
                ImagePicker.with(requireActivity())
                    .cameraOnly()
                    .crop()
                    .compress(1024)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            },
            btnGalleryClick = {
                launcher.launch(
                    PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        .build()
                )
            }
        )
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val uri = data?.data!!
                    mUri = CommonUtils.correctImageOrientation(requireContext(), uri)
                    Glide.with(requireContext()).load(uri)
                        .placeholder(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.mipmap.ic_add_pets
                            )
                        )
                        .into(binding.ivPetImage)
                }

                ImagePicker.RESULT_ERROR -> {
                    CommonUtils.showCustomToast(binding.root, ImagePicker.getError(data), false)
                }

                else -> {
                    CommonUtils.showCustomToast(binding.root, "Task Cancelled", false)
                }
            }
        }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}