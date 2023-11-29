package com.example.unit_converter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val sharedViewModel: UnitTypeModel by activityViewModels()
    private val originalButtonIds = listOf(
        R.id.length, R.id.angle, R.id.mass, R.id.surface, R.id.amperage,
        R.id.pressure, R.id.time, R.id.velocity, R.id.temperature
    )

    private var currentButtonIds = originalButtonIds.toMutableList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        for (buttonId in currentButtonIds) {
            val button = rootView.findViewById<Button>(buttonId)
            button.setOnClickListener {
                sharedViewModel.setType(resources.getResourceEntryName(buttonId))
                Navigation.findNavController(rootView).navigate(R.id.navigateToConverter)
            }
        }

        val searchView = rootView.findViewById<SearchView>(R.id.search)
        rootView.findViewById<GridLayout>(R.id.gridLayout)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterButtons(newText)
                return true
            }
        })

        return rootView
    }

    private fun filterButtons(query: String?) {
        currentButtonIds = if (query.isNullOrBlank()) {
            originalButtonIds.toMutableList()
        } else {
            originalButtonIds.filter {
                resources.getResourceEntryName(it).contains(query, ignoreCase = true)
            }.toMutableList()
        }

        updateButtonsVisibility()
    }

    private fun updateButtonsVisibility() {
        for (buttonId in originalButtonIds) {
            val button = view?.findViewById<Button>(buttonId)
            if (button != null) {
                button.visibility = if (currentButtonIds.contains(buttonId)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

}