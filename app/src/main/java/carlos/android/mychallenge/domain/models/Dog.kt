package carlos.android.mychallenge.domain.models

import android.net.Uri
import carlos.android.mychallenge.domain.Gender

data class Dog(
    val uniqueId: Int = 0,
    val name: String,
    val age: Int,
    val gender: Gender,
    val personality: String,
    val photoUri: String?,
)
