package com.metalichecky.amonguseditor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.metalichecky.amonguseditor.R
import com.metalichecky.amonguseditor.model.item.Hat
import com.metalichecky.amonguseditor.model.item.Item
import com.metalichecky.amonguseditor.model.item.Pet
import com.metalichecky.amonguseditor.model.item.Skin
import com.metalichecky.amonguseditor.repo.AssetRepo
import com.metalichecky.amonguseditor.util.TypefaceUtils
import com.metalichecky.amonguseditor.util.setCustomTypeface
import kotlinx.android.synthetic.main.layout_item_hat.view.*
import kotlinx.android.synthetic.main.layout_item_pet.view.*
import kotlinx.android.synthetic.main.layout_item_skin.view.*

class ItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {

    }

    var items: List<Item> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: Listener? = null

    override fun getItemViewType(position: Int): Int {
        return when (items.getOrNull(position)) {
            is Hat -> {
                ViewTypes.VIEW_HAT.ordinal
            }
            is Skin -> {
                ViewTypes.VIEW_SKIN.ordinal
            }
            else -> ViewTypes.VIEW_PET.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (viewType) {
            ViewTypes.VIEW_HAT.ordinal -> {
                val view = inflater.inflate(R.layout.layout_item_hat, parent, false)
                HatViewHolder(view)
            }
            ViewTypes.VIEW_SKIN.ordinal -> {
                val view = inflater.inflate(R.layout.layout_item_skin, parent, false)
                SkinViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.layout_item_pet, parent, false)
                PetViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items?.getOrNull(holder.adapterPosition) ?: return

        if (holder is HatViewHolder && item is Hat) {
            holder.bind(item)
        } else if (holder is SkinViewHolder && item is Skin) {
            holder.bind(item)
        } else if (holder is PetViewHolder && item is Pet) {
            holder.bind(item)
        }
    }

    enum class ViewTypes {
        VIEW_HAT,
        VIEW_SKIN,
        VIEW_PET
    }

    inner class HatViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ivHatImage = view.ivHatImage
        val tvHatName = view.tvHatName
        val cvContainer = view.cvContainerHat

        fun bind(hat: Hat) {
            val backgroundColor = if (hat.selected) {
                ContextCompat.getColor(view.context, R.color.colorItemBackgroundSelected)
            } else {
                ContextCompat.getColor(view.context, R.color.colorItemBackground)
            }
            cvContainer.setCardBackgroundColor(backgroundColor)
            cvContainer.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClicked(hat, adapterPosition)
                }
            }

            tvHatName.setCustomTypeface(TypefaceUtils.TypeFaces.AMATIC_BOLD)
            tvHatName.setText(hat.name)
            AssetRepo.getAssetDrawable(hat)?.let {
                ivHatImage.setImageDrawable(it)
            }
        }
    }

    inner class SkinViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ivSkinImage = view.ivSkinImage
        val tvSkinName = view.tvSkinName
        val cvContainer = view.cvContainerSkin

        fun bind(skin: Skin) {
            val backgroundColor = if (skin.selected) {
                ContextCompat.getColor(view.context, R.color.colorItemBackgroundSelected)
            } else {
                ContextCompat.getColor(view.context, R.color.colorItemBackground)
            }
            cvContainer.setCardBackgroundColor(backgroundColor)
            cvContainer.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClicked(skin, adapterPosition)
                }
            }

            tvSkinName.setCustomTypeface(TypefaceUtils.TypeFaces.AMATIC_BOLD)
            tvSkinName.setText(skin.name)
            AssetRepo.getAssetDrawable(skin)?.let {
                ivSkinImage.setImageDrawable(it)
            }
        }
    }

    inner class PetViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ivPetImage = view.ivPetImage
        val tvPetName = view.tvPetName
        val cvContainer = view.cvContainerPet

        fun bind(pet: Pet) {
            val backgroundColor = if (pet.selected) {
                ContextCompat.getColor(view.context, R.color.colorItemBackgroundSelected)
            } else {
                ContextCompat.getColor(view.context, R.color.colorItemBackground)
            }
            cvContainer.setCardBackgroundColor(backgroundColor)
            cvContainer.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClicked(pet, adapterPosition)
                }
            }

            tvPetName.setCustomTypeface(TypefaceUtils.TypeFaces.AMATIC_BOLD)
            tvPetName.setText(pet.name)
            AssetRepo.getAssetDrawable(pet)?.let {
                ivPetImage.setImageDrawable(it)
            }
        }
    }

    interface Listener {
        fun onClicked(item: Item, position: Int)
    }
}