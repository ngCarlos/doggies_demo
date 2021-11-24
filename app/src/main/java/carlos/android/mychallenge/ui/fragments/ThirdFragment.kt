package carlos.android.mychallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import carlos.android.mychallenge.R
import carlos.android.mychallenge.databinding.FragmentThirdBinding
import carlos.android.mychallenge.domain.models.ViewState
import carlos.android.mychallenge.viewmodel.DogsListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class ThirdFragment  : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    private val dogsListViewModel: DogsListViewModel by sharedViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dogUniqueId = arguments?.getInt(DOG_UNIQUE_ID) ?: 0
        dogsListViewModel.viewStateLiveData.observe(viewLifecycleOwner, Observer { viewState ->
            render(viewState)
        })
        dogsListViewModel.onStartWithData(dogUniqueId)
    }

    private fun render(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> {

            }
            is ViewState.DogData -> {
                binding.dogNameTitle.text = viewState.dog.name
                binding.dogAgeTextview.setText(
                    getString(R.string.dog_age_display, viewState.dog.age))
                binding.dogPersonalityTextview.setText(
                    getString( R.string.dog_personality_display, viewState.dog.personality)
                )
                binding.dogGenderTextview.setText(
                    getString( R.string.dog_gender_display, viewState.dog.gender.name)
                )
                binding.dogPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.dogPhoto.setImageURI(viewState.dog.photoUri?.toUri())

            }
            else -> {
                Timber.e(viewState.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dogsListViewModel.startRetrievingDogs()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}