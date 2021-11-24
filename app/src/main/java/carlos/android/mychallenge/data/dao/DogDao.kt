package carlos.android.mychallenge.data.dao

import androidx.room.*
import carlos.android.mychallenge.data.models.DogEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DogDao {
    @Query("SELECT * FROM DogEntity")
    fun fetchDogs(): List<DogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDog(dog: DogEntity)

    @Query("SELECT * FROM DogEntity WHERE uniqueId = :id")
    fun fetchDog(id: Int): DogEntity

    @Update
    fun updateDog(dogEntity: DogEntity): Int

    @Query("DELETE FROM DogEntity WHERE uniqueId = :id")
    fun deleteDog(id: Int): Int
}