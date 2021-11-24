package carlos.android.mychallenge.viewmodels

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import carlos.android.mychallenge.domain.DogsRepository
import carlos.android.mychallenge.domain.EventType
import carlos.android.mychallenge.domain.models.ViewState
import carlos.android.mychallenge.rules.MainCoroutineRule
import carlos.android.mychallenge.viewmodel.DogsListViewModel
import carlos.android.mychallenge.viewmodel.GENDER
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_DOG
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_DOGS_LIST
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_DOG_AGE
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_DOG_AGE_INVALID_STRING
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_DOG_AGE_STRING
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_DOG_GENDER
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_DOG_NAME
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_DOG_PERSONALITY
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_FEMALE_GENDER
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_INVALID_DOG_ID
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_INVALID_GENDER_DOG
import carlos.android.mychallenge.viewmodels.DogTestData.TEST_VALID_DOG_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DogListViewModelTest {
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutine = MainCoroutineRule.TestCoroutineRule()

    @Mock
    lateinit var dogsRepository: DogsRepository
    @Mock
    lateinit var uriMock: Uri

    lateinit var dogsListViewModel: DogsListViewModel

    @Before
    fun setUp() {
        dogsListViewModel = DogsListViewModel(dogsRepository)
    }

    @Test
    fun `fetch all available dogs`() {
        testCoroutine.runBlockingTest {
            // WHEN
            doReturn(TEST_DOGS_LIST).`when`(dogsRepository).fetchDogs()
            dogsListViewModel.startRetrievingDogs()

            // THEN
            verify(dogsRepository).fetchDogs()
            assertNotNull(dogsListViewModel.viewStateLiveData.value)
            assertEquals(ViewState.DogsResults(TEST_DOGS_LIST),
                dogsListViewModel.viewStateLiveData.value)
        }
    }

    @Test
    fun `saveDog saves a dog and change the state to Add`() {
        // GIVEN
        dogsListViewModel.onDogNameChanged(TEST_DOG_NAME)
        dogsListViewModel.onDogAgeChanged(TEST_DOG_AGE.toString())
        dogsListViewModel.onGenderChanged(TEST_DOG_GENDER.name)
        dogsListViewModel.onDogPersonalityChanged(TEST_DOG_PERSONALITY)

        testCoroutine.runBlockingTest {
            // WHEN
            doReturn(Unit).`when`(dogsRepository).saveDog(TEST_DOG)
            dogsListViewModel.saveDog()

            // THEN
            verify(dogsRepository).saveDog(TEST_DOG)
            assertNotNull(dogsListViewModel.viewStateLiveData.value)
            assertEquals(ViewState.EventTypeState(EventType.ADD),
                dogsListViewModel.viewStateLiveData.value)
        }
    }

    @Test
    fun `onStartWithData retrieves a dog with a valid id`() {
        // GIVEN

        testCoroutine.runBlockingTest {
            // WHEN
            doReturn(TEST_DOG).`when`(dogsRepository).fetchDog(TEST_VALID_DOG_ID)
            dogsListViewModel.onStartWithData(TEST_VALID_DOG_ID)

            // THEN
            verify(dogsRepository).fetchDog(TEST_VALID_DOG_ID)
            assertNotNull(dogsListViewModel.viewStateLiveData.value)
            assertEquals(ViewState.DogData(TEST_DOG), dogsListViewModel.viewStateLiveData.value)
        }
    }

    @Test
    fun `onStartWithData does not retrieve any dog with an invalid id`() {
        // GIVEN

        // WHEN
        dogsListViewModel.onStartWithData(TEST_INVALID_DOG_ID)

        // THEN
        testCoroutine.runBlockingTest {
            verify(dogsRepository, never()).fetchDog(TEST_INVALID_DOG_ID)
        }
    }

    @Test
    fun `onGenderChanged matches a valid enum value allows to save a dog`() {
        // GIVEN
        dogsListViewModel.onDogNameChanged(TEST_DOG_NAME)
        dogsListViewModel.onDogAgeChanged(TEST_DOG_AGE.toString())
        dogsListViewModel.onDogPersonalityChanged(TEST_DOG_PERSONALITY)

        // WHEN
        dogsListViewModel.onGenderChanged(TEST_FEMALE_GENDER)
        testCoroutine.runBlockingTest {
            doReturn(Unit).`when`(dogsRepository).saveDog(TEST_DOG)
            dogsListViewModel.saveDog()

            // THEN
            verify(dogsRepository).saveDog(TEST_DOG)
            assertNotNull(dogsListViewModel.viewStateLiveData.value)
            assertEquals(ViewState.EventTypeState(EventType.ADD),
                dogsListViewModel.viewStateLiveData.value)
        }
    }

    @Test
    fun `onGenderChanged matches an invalid enum value allows to save a dog`() {
        // GIVEN
        dogsListViewModel.onDogNameChanged(TEST_DOG_NAME)
        dogsListViewModel.onDogAgeChanged(TEST_DOG_AGE.toString())
        dogsListViewModel.onDogPersonalityChanged(TEST_DOG_PERSONALITY)

        // WHEN
        dogsListViewModel.onGenderChanged(GENDER)
        testCoroutine.runBlockingTest {
            dogsListViewModel.saveDog()

            // THEN
            verify(dogsRepository, never()).saveDog(TEST_INVALID_GENDER_DOG)
        }
    }

    @Test
    fun `onDogAgeChanged with a valid input allows to save a dog`() {
        // GIVEN
        dogsListViewModel.onDogNameChanged(TEST_DOG_NAME)
        dogsListViewModel.onGenderChanged(TEST_DOG_GENDER.name)
        dogsListViewModel.onDogPersonalityChanged(TEST_DOG_PERSONALITY)

        // WHEN
        dogsListViewModel.onDogAgeChanged(TEST_DOG_AGE_STRING)
        testCoroutine.runBlockingTest {
            doReturn(Unit).`when`(dogsRepository).saveDog(TEST_DOG.copy(age = TEST_DOG_AGE))
            dogsListViewModel.saveDog()

            // THEN
            verify(dogsRepository).saveDog(TEST_DOG.copy(age = TEST_DOG_AGE))
            assertNotNull(dogsListViewModel.viewStateLiveData.value)
            assertEquals(ViewState.EventTypeState(EventType.ADD),
                dogsListViewModel.viewStateLiveData.value)
        }
    }

    @Test
    fun `onDogAgeChanged with an invalid input does not allow to save a dog`() {
        // GIVEN
        dogsListViewModel.onDogNameChanged(TEST_DOG_NAME)
        dogsListViewModel.onGenderChanged(TEST_DOG_GENDER.name)
        dogsListViewModel.onDogPersonalityChanged(TEST_DOG_PERSONALITY)

        // WHEN
        dogsListViewModel.onDogAgeChanged(TEST_DOG_AGE_INVALID_STRING)
        testCoroutine.runBlockingTest {
            dogsListViewModel.saveDog()

            // THEN
            verify(dogsRepository, never()).saveDog(TEST_DOG)
        }
    }
}