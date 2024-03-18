package com.example.demo3m.ui.firstScreen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.demo3m.R
import com.example.demo3m.databinding.FragmentFirstBinding
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.demo3m.data.enums.LoadingState
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FirstFragment : Fragment() {
    private val firstFragmentViewModel: FirstViewModel by viewModels()
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUI()

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        val searchView = binding.toolbar.menu.findItem(R.id.action_search).actionView as SearchView

        checkLocationPermission()

        binding.btnChangeUnits.setOnClickListener {
            firstFragmentViewModel.changeUnits()
        }

        binding.toolbar.setOnClickListener {
            animateView(it, 0f, 1f) // animate in
            binding.toolbar.menu.findItem(R.id.action_search).expandActionView()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    val searchView = menuItem.actionView as SearchView
                    searchView.setOnSearchClickListener {
                        animateView(it, 0f, 1f)
                    }
                    searchView.setOnCloseListener {
                        animateView(searchView, 1f, 0f)
                        false
                    }
                    true
                }

                else -> false
            }
        }


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    firstFragmentViewModel.onChangeLocationValue(query)
                }

                firstFragmentViewModel.fetchWeather()
                Log.i("FirstFragment", "Search query: $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    firstFragmentViewModel.onChangeLocationValue(newText)
                }

                Log.i("FirstFragment", "Search query text change: $newText")
                return false
            }
        })
        return binding.root
    }

    private fun initUI() {
        lifecycleScope.launch {
            firstFragmentViewModel.weatherData.observe(viewLifecycleOwner) {
                binding.tvLocation.text = it.name ?: "No location name found"
                binding.tvTermalSense.text = "${it.temperature}"
            }

            firstFragmentViewModel.loadingState.observe(viewLifecycleOwner) {
                setProgressBarStatus()
            }

            firstFragmentViewModel.units.observe(viewLifecycleOwner) {
                binding.tvUnits.text = if (it != "imperial") " C" else " F"
                binding.btnChangeUnits.text = if (it != "imperial") " Change Farenheit" else " Change Celsius"
            }
        }
    }

    private fun animateView(view: View, start: Float, end: Float) {
        view.alpha = start
        view.animate()
            .alpha(end)
            .setDuration(500)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun setProgressBarStatus() {

        firstFragmentViewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                LoadingState.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.isIndeterminate = true
                    binding.progressBar.progressTintList = null
                    binding.progressBar.animate()
                }
                LoadingState.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.isIndeterminate = false
                }
                LoadingState.ERROR -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.isIndeterminate = false
                    binding.progressBar.progress = 100
                    binding.progressBar.animate()
                    binding.progressBar.progressTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.red)

                    lifecycleScope.launch {
                        delay(2000)
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            fetchLocation()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    fetchLocation()
                }
                return
            }
        }
    }

    private fun fetchLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                Log.i("FirstFragment", "Location: $location")
                Log.i("FirstFragment", "Location separated: ${location.latitude},${location.longitude}")
                if (location != null) {
                    firstFragmentViewModel.onChangeLocationValue("${location.latitude},${location.longitude}")
                    firstFragmentViewModel.fetchWeather()
                }
            }
    }
}

