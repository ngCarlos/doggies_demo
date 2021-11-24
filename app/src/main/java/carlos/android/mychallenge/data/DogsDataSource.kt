package carlos.android.mychallenge.data

import androidx.core.net.toUri
import carlos.android.mychallenge.data.dao.DogDao
import carlos.android.mychallenge.data.models.DogEntity
import carlos.android.mychallenge.domain.DogsRepository
import carlos.android.mychallenge.domain.Gender
import carlos.android.mychallenge.domain.models.Dog

class DogsDataSource(private val dao: DogDao): DogsRepository {

    override suspend fun fetchDogs(): List<Dog> {
        return dao.fetchDogs()
            .map { entity ->
                Dog(
                    entity.uniqueId,
                    entity.dogName,
                    entity.dogAge,
                    Gender.valueOf(entity.dogGender),
                    entity.dogPersonality,
                    entity.photoUri,
                )
            }
    }

    override suspend fun saveDog(dog: Dog) {
        dao.saveDog(DogEntity(
            dogName = dog.name,
            dogAge = dog.age,
            dogGender = dog.gender.name,
            dogPersonality = dog.personality,
            photoUri = dog.photoUri,
        ))
    }

    override suspend fun fetchDog(id: Int): Dog {
        return with (dao.fetchDog(id)) {
            Dog( uniqueId, dogName, dogAge, Gender.valueOf(dogGender), dogPersonality, photoUri,)
        }
    }

    override suspend fun deleteDog(id: Int): Int {
        return dao.deleteDog(id)
    }

}