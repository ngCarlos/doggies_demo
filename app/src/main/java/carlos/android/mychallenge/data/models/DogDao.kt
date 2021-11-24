package carlos.android.mychallenge.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class DogEntity(
    @PrimaryKey(autoGenerate = true)
    val uniqueId: Int = 0,
    val dogName: String,
    val dogAge: Int,
    val dogGender: String,
    val dogPersonality: String,
    val photoUri: String?,
)
