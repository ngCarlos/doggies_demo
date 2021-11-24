package carlos.android.mychallenge.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import carlos.android.mychallenge.R
import carlos.android.mychallenge.databinding.DogInfoItemBinding
import carlos.android.mychallenge.databinding.EmptyListItemBinding
import carlos.android.mychallenge.domain.models.Dog
import carlos.android.mychallenge.ui.adapters.listeners.DogActionListener

private const val EMPTY_TYPE = 0
private const val DOG_TYPE = 1

class DogsAdapter(
    private val dogList: MutableList<Dog> = mutableListOf<Dog>(),
    private val dogActionListener: DogActionListener,
) : RecyclerView.Adapter<BaseBindingItemViewHolder<*>>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingItemViewHolder<*> {
        context = parent.context
        return when (viewType) {
            EMPTY_TYPE -> {
                val emptyBinding = EmptyListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EmptyViewBindingViewHolder(emptyBinding)
            }
            DOG_TYPE -> {
                val dogBinding = DogInfoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DogViewBindingViewHolder(dogBinding)
            }
            else -> {
                throw IllegalArgumentException("The list has 0 elements")
            }
        }
    }

    override fun onBindViewHolder(holder: BaseBindingItemViewHolder<*>, position: Int) {
        when(holder) {
            is EmptyViewBindingViewHolder -> {
                holder.bind(EMPTY_TYPE, position)
            }
            is DogViewBindingViewHolder -> {
                holder.bind(dogList[position] ,position)
            }
            else -> throw IllegalArgumentException("Invalid option on binding view holder")
        }
    }

    override fun getItemCount(): Int {
        return if (dogList.isEmpty()) {
            1
        } else {
            dogList.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dogList.isEmpty()) {
            EMPTY_TYPE
        } else {
            DOG_TYPE
        }
    }

    fun updateList(updatedDogList: List<Dog>) {
        //val oldList = dogList
        //val diffResult = DiffUtil.calculateDiff(DogsDiffCallback(oldList, updatedDogList))
        dogList.clear()
        dogList.addAll(updatedDogList)
        notifyDataSetChanged()
        //diffResult.dispatchUpdatesTo(this)
    }

    //ViewHolders with ViewBinding

    inner class EmptyViewBindingViewHolder(binding: EmptyListItemBinding): BaseBindingItemViewHolder<Int>(binding) {
        override fun bind(item: Int, position: Int) {
            // Needs to be empty
        }
    }

    inner class DogViewBindingViewHolder(val binding: DogInfoItemBinding): BaseBindingItemViewHolder<Dog>(binding) {
        override fun bind(item: Dog, position: Int) {
            binding.dogListImage.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.labrador))
            binding.dogListName.text = item.name
            binding.dogListPersonality.text = item.personality
            binding.dogListImage.setImageURI(item.photoUri?.toUri())
            binding.root.setOnClickListener {
                dogActionListener.onItemClick(dogList[position].uniqueId.toString())
            }
        }
    }
}
