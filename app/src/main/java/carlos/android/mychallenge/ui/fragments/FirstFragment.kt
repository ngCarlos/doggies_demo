package carlos.android.mychallenge.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.media.tv.TvInputManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import carlos.android.mychallenge.R
import carlos.android.mychallenge.databinding.FragmentFirstBinding
import carlos.android.mychallenge.domain.models.ViewState
import carlos.android.mychallenge.ui.adapters.DogsAdapter
import carlos.android.mychallenge.ui.adapters.listeners.DogActionListener
import carlos.android.mychallenge.viewmodel.DogsListViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.lang.IllegalArgumentException


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), DogActionListener {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val dogsListViewModel: DogsListViewModel by sharedViewModel()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dogList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = DogsAdapter( dogActionListener = this@FirstFragment)
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
        dogsListViewModel.viewStateLiveData.observe(viewLifecycleOwner, Observer { viewState ->
            render(viewState)
        })
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestPermission()) {
            // TODO: configure the adapter at this point.
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),102)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            102 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // TODO: configure the adapter at this point if the permission were not granted
                }
            }
        }
    }

    private fun requestPermission() = ContextCompat.checkSelfPermission(
        requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun render(viewState: ViewState) {
        when (viewState) {
            is ViewState.LoadingState -> {
                binding.dogProgress.visibility = View.VISIBLE
                binding.dogList.visibility = View.GONE
            }
            is ViewState.DogsResults -> {
                binding.dogProgress.visibility = View.GONE
                binding.dogList.visibility = View.VISIBLE
                (binding.dogList.adapter as DogsAdapter).updateList(viewState.dogsList)
            }
            else -> { }
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

    override fun onItemClick(id: String) {
        val bundle = bundleOf()
        bundle.apply {
            putInt(DOG_UNIQUE_ID, id.toInt())
        }
        dogsListViewModel.resetState()
        findNavController().navigate(R.id.action_FirstFragment_to_ThirdFragment, bundle)
    }
}