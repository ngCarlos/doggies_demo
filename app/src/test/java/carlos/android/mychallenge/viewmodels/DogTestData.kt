package carlos.android.mychallenge.viewmodels

import carlos.android.mychallenge.domain.Gender
import carlos.android.mychallenge.domain.models.Dog

object DogTestData {
    //Testing values

    const val TEST_VALID_DOG_ID = 167
    const val TEST_INVALID_DOG_ID = 0

    const val TEST_FEMALE_GENDER = "Female"

    const val TEST_DOG_NAME = "Atka"
    const val TEST_DOG_AGE = 13
    const val TEST_DOG_AGE_STRING = "13"
    const val TEST_DOG_AGE_INVALID_STRING = "A"

    val TEST_DOG_GENDER = Gender.FEMALE
    const val TEST_DOG_PERSONALITY = "easy going"

    val TEST_DOGS_LIST = listOf(
        Dog(uniqueId = 1, name = "Rosco", age = 3, gender = Gender.MALE, personality = "easy going", null),
        Dog(uniqueId = 2, name = "Princess", age = 13, gender = Gender.FEMALE, personality = "sassy", null),
        Dog(uniqueId = 3, name = "Trust", age = 6, gender = Gender.FEMALE, personality = "happy and playful", null),
    )

    val TEST_DOG = Dog(
        name = TEST_DOG_NAME,
        age = TEST_DOG_AGE,
        gender = TEST_DOG_GENDER,
        personality = TEST_DOG_PERSONALITY,
        photoUri = null
    )
    val TEST_INVALID_GENDER_DOG = TEST_DOG.copy(gender = Gender.NOT_SET)

}