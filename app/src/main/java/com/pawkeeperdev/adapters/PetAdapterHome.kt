package com.pawkeeperdev.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pawkeeperdev.config.CommonUtils
import com.pawkeeperdev.entities.PetDataModel
import com.pawkeeperdev.R
import com.pawkeeperdev.databinding.ItemviewPetLogoHomeBinding

class PetAdapterHome(
    private val context: Context,
    private var petList: ArrayList<PetDataModel>,
    private var onClick: OnItemClick
) :

    RecyclerView.Adapter<PetAdapterHome.ViewHolder>() {
    private lateinit var binding: ItemviewPetLogoHomeBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemviewPetLogoHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        val originalData = petList[position]
        val image = CommonUtils.byteArrayToBitmap(originalData.petImage)
        Glide.with(context)
            .load(image)
            .placeholder(R.color.white_light)
            .into(holder.binding.ivPetLogo)
        holder.binding.tvPetName.text = originalData.petName
        holder.binding.tvPetType.text = originalData.petType

        holder.binding.root.setOnClickListener {
            onClick.onClickIcon(position)
        }

        holder.binding.btnLike.setOnClickListener {
            val isLiked = if (originalData.isLiked==1)0 else 1
            onClick.onLikeClick(position,isLiked)
        }

        if (originalData.isLiked == 1) {
            holder.binding.btnLike.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.heart_fill
                )
            )
            holder.binding.btnLike.setColorFilter(ContextCompat.getColor(context, R.color.color_primary), PorterDuff.Mode.SRC_IN)
            holder.binding.tvLike.text = context.getString(R.string.str_liked)
            holder.binding.tvLike.setTextColor(ContextCompat.getColor(context,R.color.color_primary))
        } else {
            holder.binding.btnLike.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.heart
                )
            )
            holder.binding.tvLike.text = context.getString(R.string.str_like)
            holder.binding.tvLike.setTextColor(ContextCompat.getColor(context,R.color.black_light))
            holder.binding.btnLike.setColorFilter(ContextCompat.getColor(context, R.color.black_light), PorterDuff.Mode.SRC_IN)
        }
    }

    fun updateItem(position: Int, newIsLiked: Int) {
        petList[position].isLiked = newIsLiked
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    inner class ViewHolder(val binding: ItemviewPetLogoHomeBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClick {
        fun onClickIcon(position: Int)
        fun onLikeClick(position: Int,isLiked : Int)
    }

}