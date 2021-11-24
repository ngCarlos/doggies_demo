package carlos.android.mychallenge.domain

import carlos.android.mychallenge.domain.models.Dog

interface DogsRepository {

    /**
     * Fetches all dogs stored
     */
    suspend fun fetchDogs(): List<Dog>

    /**
     * Save all the dog info
     */
    suspend fun saveDog(dog: Dog)

    /**
     * Retrieves the dog with the given id
     */
    suspend fun fetchDog(id: Int): Dog

    /**
     * Deletes the dog with the given id
     */
    suspend fun deleteDog(id: Int): Int
}