package com.pawkeeperdev.activities.dashboard.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pawkeeperdev.activities.dashboard.ui.profile.ProfileViewModel
import com.pawkeeperdev.activities.signIn.SignInActivity
import com.pawkeeperdev.adapters.PetAdapterHome
import com.pawkeeperdev.config.CommonUtils
import com.pawkeeperdev.config.TouchEffectListener
import com.pawkeeperdev.entities.PetDataModel
import com.pawkeeperdev.R
import com.pawkeeperdev.activities.addPets.AddPetActivity
import com.pawkeeperdev.activities.dashboard.DashBoardActivity
import com.pawkeeperdev.config.applyTopAndSideInsets
import com.pawkeeperdev.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var petAdapter: PetAdapterHome
    private var id: Long = 0
    private val viewModel: HomeViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private var lisOfPets: List<PetDataModel> = emptyList()
    private var petListWithStaticData = ArrayList<PetDataModel>()

    private val listener = object : PetAdapterHome.OnItemClick {
        override fun onClickIcon(position: Int) {
            CommonUtils.addLogE("RoomPetData", "listener : $position")
        }

        override fun onLikeClick(position: Int, isLiked: Int) {
            val pet = petListWithStaticData[position]
            pet.isLiked = isLiked
            viewModel.updatePet(pet)
            petAdapter.updateItem(position, isLiked)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.isVisible = true

        binding.btnMorePets.setOnTouchListener(TouchEffectListener())
        binding.btnAddPet.setOnTouchListener(TouchEffectListener())
        binding.btnAddMorePet.setOnTouchListener(TouchEffectListener())
        binding.btnLikedPets.setOnTouchListener(TouchEffectListener())
        binding.btnLogOut.setOnTouchListener(TouchEffectListener())
        binding.llTopDetail.setOnTouchListener(TouchEffectListener())
        binding.btnHideMoreOption.setOnTouchListener(TouchEffectListener())


        binding.btnMorePets.setOnClickListener(this)
        binding.btnAddPet.setOnClickListener(this)
        binding.btnAddMorePet.setOnClickListener(this)
        binding.btnLikedPets.setOnClickListener(this)
        binding.btnLogOut.setOnClickListener(this)
        binding.llTopDetail.setOnClickListener(this)
        binding.btnHideMoreOption.setOnClickListener(this)

        viewModel.usersWithPet()

        if (activity?.intent?.getLongExtra(DashBoardActivity.KEY_EXTRA, 0) != null) {
            id = activity?.intent?.getLongExtra(DashBoardActivity.KEY_EXTRA, 0)!!
        }


        viewModel.getPetsByUserId(id)
        viewModel.pets.observe(viewLifecycleOwner) { petList ->
            lisOfPets = petList
            initView(petList)
            binding.progressBar.isVisible = false
            for (i in petList.indices) {
                CommonUtils.addLogE("RoomPetData", "petList : ${petList[i]}")
            }
        }

        viewModel.likedPets.observe(viewLifecycleOwner) { petList ->
            lisOfPets = petList
            initView(petList)
            for (i in petList.indices) {
                CommonUtils.addLogE("RoomPetData", "petList : ${petList[i]}")
            }
        }

        viewModel.usersWithPets.observe(viewLifecycleOwner) { usersWithPets ->
            for (i in usersWithPets.indices) {
                CommonUtils.addLogE("RoomUserPetDataModel", "UsersWithPets : ${usersWithPets[i]}")
            }
        }

        binding.swiper.setOnRefreshListener {
            viewModel.getPetsByUserId(id)
            binding.btnLikedPets.text = requireContext().getString(R.string.str_liked_pets)
            binding.swiper.isRefreshing = true
        }


        profileViewModel.logOutResult.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { message ->
                CommonUtils.showCustomToast(binding.root, message, true)
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }, onFailure = { exception ->
                CommonUtils.showCustomToast(binding.root, exception.message, true)
            })
        }

        binding.recyclerHomePetIcon.setOnClickListener {
            binding.llMoreOptions.visibility = View.GONE
        }

    }


    private fun initView(list: List<PetDataModel>) {
        //Android 15 edge support
        binding.llTopDetail.applyTopAndSideInsets()

        binding.swiper.isRefreshing = false
        binding.llMoreOptions.visibility = View.GONE
        binding.llMyPets.visibility = View.GONE
        binding.llTopDetail.visibility = View.GONE
        binding.btnAddPet.visibility = View.GONE

        binding.recyclerHomePetIcon.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        petListWithStaticData = ArrayList()
        /*val staticData = PetDataModel(
            ownerId = 0,
            petId = 0,
            petType = "Static",
            petImage = CommonUtils.giveByteAddImage(requireContext()),
            petName = "Static",
            petAdoptDate = "0/0/0")
        petListWithStaticData.add(staticData)*/


        petListWithStaticData.addAll(list)
        for (i in petListWithStaticData.indices) {
            CommonUtils.addLogE("RoomPetData", "petList after update : ${petListWithStaticData[i]}")
        }
        petAdapter =
            PetAdapterHome(
                requireContext(),
                petListWithStaticData,
                listener
            )
        binding.recyclerHomePetIcon.adapter = petAdapter

        if (lisOfPets.isEmpty()) {
            binding.llMyPets.visibility = View.GONE
            binding.llTopDetail.visibility = View.GONE
            binding.btnAddPet.visibility = View.VISIBLE
            binding.progressBar.isVisible = false
        } else {
            binding.llMyPets.visibility = View.VISIBLE
            binding.llTopDetail.visibility = View.VISIBLE
            binding.btnAddPet.visibility = View.GONE
            binding.progressBar.isVisible = false
        }
    }


    override fun onClick(v: View?) {
        val btnId = v?.id
        if (btnId == R.id.btnMorePets || btnId == R.id.llTopDetail) {
            if (binding.llMoreOptions.visibility == View.GONE) {
                binding.llMoreOptions.visibility = View.VISIBLE
                binding.btnMorePets.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_upper_arrow
                    )
                )
            } else {
                binding.llMoreOptions.visibility = View.GONE
                binding.btnMorePets.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_down_arrow
                    )
                )
            }
        } else if (btnId == R.id.btnAddPet) {
            if (id != 0L) {
                startActivity(
                    Intent(requireContext(), AddPetActivity::class.java).putExtra(
                        AddPetActivity.EXTRA_DATA_PET,
                        id
                    )
                )
            }
        } else if (btnId == R.id.btnAddMorePet) {
            binding.llMoreOptions.visibility = View.GONE
            binding.btnMorePets.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_down_arrow
                )
            )
            startActivity(
                Intent(requireContext(), AddPetActivity::class.java)
                    .putExtra(AddPetActivity.EXTRA_DATA_PET, id)
                    .putExtra(AddPetActivity.EXTRA_WHERE, "home")
            )
        } else if (btnId == R.id.btnLikedPets) {
            binding.llMoreOptions.visibility = View.GONE
            binding.btnMorePets.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_down_arrow
                )
            )
            if (binding.btnLikedPets.text == requireContext().getString(R.string.str_liked_pets)) {
                binding.btnLikedPets.text = requireContext().getString(R.string.str_unliked_pets)
                viewModel.getLikedPetsByUserId(id, 1)
            } else {
                binding.btnLikedPets.text = requireContext().getString(R.string.str_liked_pets)
                viewModel.getLikedPetsByUserId(id, 0)
            }
            //Work to do
        } else if (btnId == R.id.btnLogOut) {
            binding.llMoreOptions.visibility = View.GONE
            //Work to do
            profileViewModel.logOut(id, 0)
        } else if (btnId == R.id.btnHideMoreOption) {
            binding.btnMorePets.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_down_arrow
                )
            )
            binding.llMoreOptions.visibility = View.GONE
        }
    }

}