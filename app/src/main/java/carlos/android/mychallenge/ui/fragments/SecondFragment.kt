package carlos.android.mychallenge.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import carlos.android.mychallenge.databinding.FragmentSecondBinding

import android.app.Activity.RESULT_OK

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import carlos.android.mychallenge.R
import carlos.android.mychallenge.domain.models.ViewState
import carlos.android.mychallenge.viewmodel.DogsListViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

const val DOG_UNIQUE_ID = "DOG_UNIQUE_ID"
const val SELECT_IMAGE = 25

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val dogsListViewModel: DogsListViewModel by sharedViewModel()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dogsListViewModel.viewStateLiveData.observe(viewLifecycleOwner, Observer { viewState ->
            render(viewState)
        })
        binding.dogPhoto.setOnClickListener {
            imageChooser()
        }
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.dog_gender)
        )
        binding.dogGender.adapter = arrayAdapter
        binding.dogGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dogsListViewModel.onGenderChanged(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Needs to be implement
            }
        }
        binding.dogNameEdittext.addTextChangedListener { newText ->
            dogsListViewModel.onDogNameChanged(newText.toString())
        }
        binding.dogAgeEdittext.addTextChangedListener { newAge ->
            dogsListViewModel.onDogAgeChanged(newAge.toString())
        }
        binding.dogPersonalityEdittext.addTextChangedListener { newText ->
            dogsListViewModel.onDogPersonalityChanged(newText.toString())
        }
        binding.saveFab.setOnClickListener {
            dogsListViewModel.saveDog()
        }
    }

    private fun imageChooser() {
        val intent = Intent().apply {
            setType("image/*")
            setAction(Intent.ACTION_GET_CONTENT)
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 25) {
            if (resultCode == RESULT_OK) {
                val imageUri = data?.data
                imageUri?.let {
                    binding.dogPhoto.setImageURI(imageUri)
                    dogsListViewModel.onPhotoUriChanged(imageUri.toString())
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun render(viewState: ViewState) {
        when (viewState) {
            is ViewState.EventTypeState -> {
                Snackbar.make(binding.root, viewState.eventType.stringResId, Snackbar.LENGTH_SHORT).show()
            }
            else -> {
                Timber.e(viewState.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}