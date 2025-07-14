package com.pawkeeperdev.activities.signup

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import com.pawkeeperdev.activities.BaseActivity
import com.pawkeeperdev.config.CommonUtils
import com.pawkeeperdev.config.TouchEffectListener
import com.pawkeeperdev.entities.UserData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pawkeeperdev.R
import com.pawkeeperdev.activities.addPets.AddPetActivity
import com.pawkeeperdev.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val cal: Calendar = Calendar.getInstance()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        binding.btnSignUp.setOnTouchListener(TouchEffectListener())
        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.cons2)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.edTvCnfPassWord.setOnEditorActionListener { _, actionId, _ ->
            var bool = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                bool = true
                signUp()
            }
            return@setOnEditorActionListener bool
        }


        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.tvBtnDob.text = CommonUtils.changeDateFormat(" dd / MMM / YYYY ", cal.time)
                CommonUtils.addLogE("DateOB", " date is ${binding.tvBtnDob.text}")
            }

        binding.tvBtnDob.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this@SignUpActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        viewModel.signUpResult.observe(this) { result ->
            result.fold(onSuccess = { userId ->
                CommonUtils.showCustomToast(
                    binding.root,
                    "User SignUp SuccessFully!",
                    isSuccess = true
                )

                val intent = Intent(this, AddPetActivity::class.java)
                intent.putExtra(AddPetActivity.EXTRA_DATA_PET, userId)
                intent.putExtra(AddPetActivity.EXTRA_WHERE, "fromSignUp")
                startActivity(intent)
                finish()
                CommonUtils.addLogE(CommonUtils.TAG, "UserIDf  : $userId")
            }, onFailure = { exception ->
                CommonUtils.showCustomToast(
                    binding.root,
                    exception.message ?: "An error occurred",
                    isSuccess = false
                )
            })
        }

    }


    private fun signUp() {
        val fullName = binding.edTvFullName.text.toString().trim()
        val userName = binding.edTvUserName.text.toString().trim()
        val email = binding.edTvEmail.text.toString().trim()
        val pass = binding.edTvPassWord.text.toString()
        val cnfPass = binding.edTvCnfPassWord.text.toString()
        val date = binding.tvBtnDob.text
        fun showErrorAndFocus(view: View, message: String) {
            view.requestFocus()
            CommonUtils.showCustomToast(binding.root, message, isSuccess = false)
        }
        when {
            fullName.isEmpty() -> showErrorAndFocus(binding.edTvFullName, "Enter Full Name")
            userName.isEmpty() -> showErrorAndFocus(binding.edTvUserName, "Enter User Name")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showErrorAndFocus(
                binding.edTvEmail,
                "Enter valid Email"
            )

            date.equals(getString(R.string.str_dd_mm_yyyy)) -> CommonUtils.showCustomToast(
                binding.root,
                "Enter your date of birth!",
                false
            )

            pass.isEmpty() -> showErrorAndFocus(binding.edTvPassWord, "Enter password")
            cnfPass.isEmpty() -> showErrorAndFocus(
                binding.edTvCnfPassWord,
                "Enter confirm password"
            )

            cnfPass != pass -> {
                CommonUtils.showCustomToast(
                    binding.root,
                    "Password doesn't match",
                    isSuccess = false
                )
                return
            }

            else -> {
                val user = UserData(
                    fullName = fullName,
                    userName = userName,
                    email = email,
                    dateOfBirth = binding.tvBtnDob.text.toString(),
                    password = pass,
                    isLogin = 1,

                    )
                viewModel.addUser(user)
            }
        }
    }

}