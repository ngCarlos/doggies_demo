package carlos.android.mychallenge.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import carlos.android.mychallenge.domain.DogsRepository
import carlos.android.mychallenge.domain.EventType
import carlos.android.mychallenge.domain.Gender
import carlos.android.mychallenge.domain.models.Dog
import carlos.android.mychallenge.domain.models.ViewState
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

@VisibleForTesting
const val GENDER = "Gender"

class DogsListViewModel(private val dogsRepository: DogsRepository): ViewModel() {
    private val _mutableViewState = MutableLiveData<ViewState>()
    val viewStateLiveData: LiveData<ViewState> = _mutableViewState

    private lateinit var dogList: List<Dog>
    private var dogName = ""
    private var dogAge = 0
    private var dogGender = Gender.NOT_SET
    private var dogPersonality = ""
    private var photoUriAsString: String? = null

    init {
        _mutableViewState.value = ViewState.LoadingState
    }

    fun startRetrievingDogs() {
        viewModelScope.launch {
            dogList = dogsRepository.fetchDogs()
            _mutableViewState.postValue(ViewState.DogsResults(dogList))
        }
    }

    fun onGenderChanged(dogGender: String) {
        val genderValue = if (dogGender.equals(GENDER)) {
            Gender.NOT_SET
        } else {
            Gender.valueOf(dogGender.uppercase())
        }
        this.dogGender = genderValue
    }

    fun onDogNameChanged(newText: String) {
        this.dogName = newText
    }

    fun onDogAgeChanged(newAge: String) {
        dogAge = try {
            newAge.toInt()
        } catch (nfe: NumberFormatException) {
            0
        }
    }

    fun onDogPersonalityChanged(newText: String) {
        this.dogPersonality = newText
    }

    fun saveDog() {
        if (dogName.isNotEmpty() &&
            dogAge > 0 &&
            dogGender != Gender.NOT_SET &&
            dogPersonality.isNotEmpty()) {
                viewModelScope.launch {
                    dogsRepository.saveDog(
                        Dog(
                            name = dogName,
                            age = dogAge,
                            gender = dogGender,
                            personality = dogPersonality,
                            photoUri = photoUriAsString,
                        )
                    )
                    _mutableViewState.value = ViewState.EventTypeState(EventType.ADD)
                }
        } else {
            _mutableViewState.value = ViewState.EventTypeState(EventType.ERROR_EMPTY)
        }
    }

    fun onStartWithData(dogUniqueId: Int) {
        if (dogUniqueId > 0) {
            viewModelScope.launch {
                val dog = dogsRepository.fetchDog(dogUniqueId)
                _mutableViewState.value = ViewState.DogData(dog)
            }
        }
    }

    fun resetState() {
        _mutableViewState.value = ViewState.LoadingState
    }

    fun onPhotoUriChanged(imageUriAsString: String) {
        photoUriAsString = imageUriAsString
    }
}