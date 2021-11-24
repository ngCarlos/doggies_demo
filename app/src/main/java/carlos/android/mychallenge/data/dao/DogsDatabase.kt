package carlos.android.mychallenge.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import carlos.android.mychallenge.data.models.DogEntity

@Database(entities = arrayOf(DogEntity::class), version = 1, exportSchema = false)
abstract class DogsDatabase: RoomDatabase() {
    abstract  fun dao(): DogDao

    companion object {
        fun createDatabase(context: Context): DogsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                DogsDatabase::class.java,
                "DogsDatabase",
            ).allowMainThreadQueries().build()
        }
    }
}