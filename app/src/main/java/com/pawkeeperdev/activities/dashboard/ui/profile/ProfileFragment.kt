package com.pawkeeperdev.activities.dashboard.ui.profile

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.pawkeeperdev.activities.signIn.SignInActivity
import com.pawkeeperdev.config.CommonUtils
import com.pawkeeperdev.config.TouchEffectListener
import com.pawkeeperdev.entities.UserData
import com.pawkeeperdev.moduels.DatabaseManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.pawkeeperdev.R
import com.pawkeeperdev.activities.dashboard.DashBoardActivity
import com.pawkeeperdev.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    var id: Long = 0
    private lateinit var dataM: DatabaseManager
    private lateinit var user: UserData
    private val viewModel: ProfileViewModel by viewModels()
    private var mUserUri: Uri? = null
    private var mBAckUri: Uri? = null
    private var isFromBAckClick = true

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        dataM = DatabaseManager(requireContext())
        binding.mainP.visibility = View.GONE
        binding.llMoreOptions.visibility = View.GONE

        binding.cardBackEdit.visibility = View.GONE
        binding.cardImgEdit.visibility = View.GONE
        binding.btnEditDone.visibility = View.GONE
        binding.btnCancelEdit.visibility = View.GONE

        binding.cardImgEdit.setOnTouchListener(TouchEffectListener())
        binding.cardBackEdit.setOnTouchListener(TouchEffectListener())
        binding.btnLogOut.setOnTouchListener(TouchEffectListener())
        binding.btnDelete.setOnTouchListener(TouchEffectListener())
        binding.btnMoreOptions.setOnTouchListener(TouchEffectListener())
        binding.btnHideMoreOption.setOnTouchListener(TouchEffectListener())
        binding.btnEdit.setOnTouchListener(TouchEffectListener())
        binding.btnEditDone.setOnTouchListener(TouchEffectListener())
        binding.btnCancelEdit.setOnTouchListener(TouchEffectListener())

        binding.cardImgEdit.setOnClickListener(this)
        binding.cardBackEdit.setOnClickListener(this)
        binding.btnLogOut.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
        binding.btnMoreOptions.setOnClickListener(this)
        binding.btnHideMoreOption.setOnClickListener(this)
        binding.btnEdit.setOnClickListener(this)
        binding.btnEditDone.setOnClickListener(this)
        binding.btnCancelEdit.setOnClickListener(this)

        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (activity?.intent?.hasExtra(DashBoardActivity.KEY_EXTRA) == true) {
            id = requireActivity().intent.getLongExtra(DashBoardActivity.KEY_EXTRA, 0)
            if (id != 0L) {
                binding.mainP.visibility = View.VISIBLE
                viewModel.getUserDataById(id)
            } else {
                binding.mainP.visibility = View.GONE
            }
        }


        viewModel.user.observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                user = userData
                if (userData.userImage != null) {
                    val image = CommonUtils.byteArrayToBitmap(userData.userImage!!)
                    binding.userImage.setImageBitmap(image)
                } else {
                    binding.userImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_place_holder_dummy
                        )
                    )
                }

                if (userData.userBackImage != null) {
                    val image = CommonUtils.byteArrayToBitmap(userData.userBackImage!!)
                    binding.userBackImage.setImageBitmap(image)
                } else {
                    binding.userBackImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.mipmap.ic_add_pets
                        )
                    )

                    binding.userBackImage.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        ), PorterDuff.Mode.SRC_IN
                    )

                }
                val age = CommonUtils.calculateAge(userData.dateOfBirth)
                binding.tvUserName.text = userData.fullName
                binding.tvId.text = requireContext().getString(R.string.str_id, userData.id)
                binding.tvUser.text = userData.userName
                binding.tvEmail.text = userData.email
                binding.tvDob.text = "DOB : ${userData.dateOfBirth} ($age years old)"
            }
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { message ->
                CommonUtils.showCustomToast(binding.root, message, true)
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
            }, onFailure = { exception ->
                CommonUtils.showCustomToast(binding.root, exception.message, true)
            })
        }


        viewModel.logOutResult.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { message ->
                CommonUtils.showCustomToast(binding.root, message, true)
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
            }, onFailure = { exception ->
                CommonUtils.showCustomToast(binding.root, exception.message, true)
            })
        }

        viewModel.updateUserResult.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { message ->
                CommonUtils.showCustomToast(binding.root, message, true)
                editUserDetails(2)
                viewModel.getUserDataById(id)
            }, onFailure = { exception ->
                CommonUtils.showCustomToast(binding.root, exception.message, true)
            })
        }


        //end onCreate
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


    // Gallery uri result
    private val launcher: ActivityResultLauncher<PickVisualMediaRequest> =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri == null) {
                CommonUtils.showCustomToast(binding.root, "Image not selected!", false)
                viewModel.getUserDataById(id)
            } else {
                if (isFromBAckClick) {
                    mBAckUri = uri
                    Glide.with(requireContext()).load(uri)
                        .placeholder(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.mipmap.ic_add_pets
                            )
                        )
                        .into(binding.userBackImage)
                    binding.userBackImage.clearColorFilter()
                    binding.userBackImage.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    isFromBAckClick = false
                } else {
                    mUserUri = uri
                    Glide.with(requireContext()).load(uri)
                        .placeholder(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_place_holder_dummy
                            )
                        )
                        .into(binding.userImage)
                }
            }
        }


    //permission result
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach { (permission, isGranted) ->
                if (permission == READ_MEDIA_IMAGES) {
                    if (isGranted) {
                        showCameraDialog()
                    } else {
                        CommonUtils.goToSetting(binding.root)
                    }
                } else {
                    if (isGranted) {
                        showCameraDialog()
                    } else {
                        CommonUtils.goToSetting(binding.root)
                    }
                }
            }
        }


    // Camera uri result
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data!!
                if (isFromBAckClick) {
                    mBAckUri = CommonUtils.correctImageOrientation(requireContext(), uri)
                    Glide.with(requireContext()).load(mBAckUri)
                        .placeholder(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.mipmap.ic_add_pets
                            )
                        )
                        .into(binding.userBackImage)
                    binding.userBackImage.clearColorFilter()
                    binding.userBackImage.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )

                    isFromBAckClick = false
                } else {
                    mUserUri = CommonUtils.correctImageOrientation(requireContext(), uri)
                    Glide.with(requireContext()).load(mUserUri)
                        .placeholder(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_place_holder_dummy
                            )
                        )
                        .into(binding.userImage)
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                CommonUtils.showCustomToast(binding.root, ImagePicker.getError(data), false)
            } else {
                CommonUtils.showCustomToast(binding.root, "Task Cancelled", false)
            }
        }

    private fun editUserDetails(flag: Int) {
        /***
        o - for default
        1 - edit
        2 - click on done
        3 - click on cancel
         */
        when (flag) {
            1 -> {
                binding.btnMoreOptions.visibility = View.GONE
                binding.btnLogOut.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE

                binding.btnEditDone.visibility = View.VISIBLE
                binding.btnCancelEdit.visibility = View.VISIBLE
                binding.cardBackEdit.visibility = View.VISIBLE
                binding.cardImgEdit.visibility = View.VISIBLE
            }

            2 -> {
                binding.btnMoreOptions.visibility = View.VISIBLE
                binding.btnLogOut.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE

                binding.btnEditDone.visibility = View.GONE
                binding.btnCancelEdit.visibility = View.GONE
                binding.cardBackEdit.visibility = View.GONE
                binding.cardImgEdit.visibility = View.GONE

            }

            3 -> {
                binding.btnMoreOptions.visibility = View.VISIBLE
                binding.btnLogOut.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE


                binding.btnEditDone.visibility = View.GONE
                binding.btnCancelEdit.visibility = View.GONE
                binding.cardBackEdit.visibility = View.GONE
                binding.cardImgEdit.visibility = View.GONE
            }

            else -> {
                binding.cardBackEdit.visibility = View.GONE
                binding.cardImgEdit.visibility = View.GONE

                binding.btnLogOut.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE

                binding.btnEditDone.visibility = View.GONE
                binding.btnCancelEdit.visibility = View.GONE
            }
        }
    }


    override fun onClick(v: View?) {
        val btnId = v?.id
        if (btnId == R.id.cardImgEdit) {
            isFromBAckClick = false
            //do click
            CommonUtils.checkPermission(requestPermissions)
        } else if (btnId == R.id.cardBackEdit) {
//            dataM.shareDatabaseFile()
//            dataM.saveDatabase()
            isFromBAckClick = true
            CommonUtils.checkPermission(requestPermissions)
        } else if (btnId == R.id.btnLogOut) {
            viewModel.logOut(id, 0)
        } else if (btnId == R.id.btnDelete) {
            viewModel.deleteSpecificUser(id)
        } else if (btnId == R.id.btnMoreOptions || btnId == R.id.llTopDetail) {
            if (binding.llMoreOptions.visibility == View.GONE) {
                binding.llMoreOptions.visibility = View.VISIBLE
                binding.btnMoreOptions.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_upper_arrow
                    )
                )
            } else {
                binding.llMoreOptions.visibility = View.GONE
                binding.btnMoreOptions.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_down_arrow
                    )
                )
            }
        } else if (btnId == R.id.btnEdit) {
            binding.llMoreOptions.visibility = View.GONE
            binding.btnMoreOptions.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_down_arrow
                )
            )
            editUserDetails(1)
        } else if (btnId == R.id.btnEditDone) {
            updateValidation()
        } else if (btnId == R.id.btnCancelEdit) {
            editUserDetails(3)
        } else if (btnId == R.id.btnHideMoreOption) {
            binding.llMoreOptions.visibility = View.GONE
            binding.btnMoreOptions.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_down_arrow
                )
            )
            editUserDetails(0)
        }
    }


    private fun updateValidation() {
        if (mUserUri != null || mBAckUri != null) {
            if (mUserUri != null) {
                val byteArray =
                    CommonUtils.resizeAndConvertToByteArray(mUserUri!!, requireContext())
                user.userImage = byteArray
                viewModel.updateUser(user)
            }
            if (mBAckUri != null) {
                val byteArray =
                    CommonUtils.resizeAndConvertToByteArray(mBAckUri!!, requireContext())
                user.userBackImage = byteArray
                viewModel.updateUser(user)
            }
        } else {
            CommonUtils.showCustomToast(binding.root, "Nothing Changed", true)
            editUserDetails(3)
        }
    }
}