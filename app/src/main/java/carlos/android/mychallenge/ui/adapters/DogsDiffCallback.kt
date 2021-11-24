package carlos.android.mychallenge.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import carlos.android.mychallenge.domain.models.Dog

class DogsDiffCallback(
    private val oldVideoList: List<Dog>,
    private val newVideoList: List<Dog>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldVideoList.size

    override fun getNewListSize(): Int = newVideoList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldVideoList.get(oldItemPosition).hashCode() == newVideoList.get(newItemPosition)
            .hashCode()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldVideoList.get(oldItemPosition) == newVideoList.get(newItemPosition)
    }
}