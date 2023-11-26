package com.example.unit_converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.unit_converter.model.UnitTypeModel
import com.google.android.material.textfield.TextInputEditText


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "converterType"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UnitConverterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UnitConverterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val sharedViewModel: UnitTypeModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_unit_converter, container, false)

        val textView = view.findViewById<TextView>(R.id.converterLabel)
        textView.text = sharedViewModel.type.value

        val button = view.findViewById<Button>(R.id.return_button)
        button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateToList)
        }

        val iconImageView: ImageView = view.findViewById(R.id.iconImageView)

        val iconType = sharedViewModel.type.value.toString()
        val resourceId = when (iconType) {
            "time" -> R.drawable.time
            "mass" -> R.drawable.mass
            "amperage" -> R.drawable.amperage
            "angle" -> R.drawable.angle
            "temperature" -> R.drawable.temperature
            "velocity" -> R.drawable.velocity
            "pressure" -> R.drawable.pressure
            "length" -> R.drawable.length
            "surface" -> R.drawable.surface
            else -> R.drawable.time
        }

        iconImageView.setImageResource(resourceId)

        val type = when (iconType) {
            "time" -> arrayOf("seconds", "minutes", "hours", "weeks")
            "mass" -> arrayOf("milligram", "gram", "kilogram", "tonne")
            "amperage" -> arrayOf("microampere", "milliampere", "ampere", "kiloampere")
            "angle" -> arrayOf("turn", "radian", "degree", "gon")
            "temperature" -> arrayOf("Celsius", "Fahrenheit", "Kelvin", "Newton")
            "velocity" -> arrayOf("mph", "kmph", "knots", "Foots per second")
            "pressure" -> arrayOf("atmospheres", "Pascals", "bars", "torrs")
            "length" -> arrayOf("meters", "foots", "yards", "miles")
            "surface" -> arrayOf("square inch", "square foot", "square yard", "square mile")
            else -> arrayOf("seconds", "minutes", "hours", "weeks")
        }


        val adapter = ArrayAdapter(requireActivity(), R.layout.drop_down_item, type)

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.unitTypePicker)
        autoCompleteTextView?.setText(type[0])
        autoCompleteTextView?.setAdapter(adapter)
        autoCompleteTextView?.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            makeConversion(iconType)
        }



        view.findViewById<TextView>(R.id.unitOneLabel).text = " ${type[0]}"
        view.findViewById<TextView>(R.id.unitTwoLabel).text = " ${type[1]}"
        view.findViewById<TextView>(R.id.unitThreeLabel).text = " ${type[2]}"
        view.findViewById<TextView>(R.id.unitFourLabel).text = " ${type[3]}"

        val valueInput = view.findViewById<TextInputEditText>(R.id.value_input)

        valueInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                makeConversion(iconType)
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        return view
    }

    fun makeConversion(type: String) {
        val selectedUnit = view?.findViewById<AutoCompleteTextView>(R.id.unitTypePicker)?.text.toString()
        val numberToConvertStr = view?.findViewById<TextInputEditText>(R.id.value_input)?.text.toString()
        var numberToConvert = 0.0
        if(numberToConvertStr != "") numberToConvert = numberToConvertStr.toDouble()
        var results = arrayOf("0", "0", "0", "0")
        when (type) {
            "time" -> results = convertTime(selectedUnit, numberToConvert)
            "mass" -> results = convertMass(selectedUnit, numberToConvert)
            "amperage" -> results = convertAmperage(selectedUnit, numberToConvert)
            "angle" -> results = convertAngle(selectedUnit, numberToConvert)
            "temperature" -> results = convertTemperature(selectedUnit, numberToConvert)
            "velocity" -> results = convertVelocity(selectedUnit, numberToConvert)
            "pressure" -> results = convertPressure(selectedUnit, numberToConvert)
            "length" -> results = convertLength(selectedUnit, numberToConvert)
            "surface" -> results = convertSurface(selectedUnit, numberToConvert)
            else -> convertTime(selectedUnit, numberToConvert)
        }
        saveConversionResults(results)
    }

    fun saveConversionResults(results: Array<String>) {
        view?.findViewById<TextView>(R.id.resultUnitOne)?.text = results[0]
        view?.findViewById<TextView>(R.id.resultUnitTwo)?.text = results[1]
        view?.findViewById<TextView>(R.id.resultUnitThree)?.text = results[2]
        view?.findViewById<TextView>(R.id.resultUnitFour)?.text = results[3]
    }

    fun convertTime(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "seconds" -> arrayOf(1.0, 1 / 60.0, 1 / 3600.0, 1 / 604800.0)
            "minutes" -> arrayOf(60.0, 1.0, 1 / 60.0, 1 / 10800.0)
            "hours" -> arrayOf(3600.0, 60.0, 1.0, 1 / 144.0)
            "weeks" -> arrayOf(604800.0, 3600.0, 60.0, 1.0)
            else -> arrayOf(1.0, 1 / 60.0, 1 / 3600.0, 1 / 604800.0)
        }

        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    fun convertMass(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "milligram" -> arrayOf(1.0, 0.001, 1e-6, 1e-9)
            "gram" -> arrayOf(1000.0, 1.0, 0.001, 1e-6)
            "kilogram" -> arrayOf(1e6, 1000.0, 1.0, 0.001)
            "tonne" -> arrayOf(1e9, 1e6, 1000.0, 1.0)
            else -> arrayOf(1.0, 0.001, 1e-6, 1e-9)
        }

        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    fun convertAmperage(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "microampere" -> arrayOf(1.0, 1e-3, 1e-6, 1e-9)
            "milliampere" -> arrayOf(1e3, 1.0, 1e-3, 1e-6)
            "ampere" -> arrayOf(1e6, 1e3, 1.0, 1e-3)
            "kiloampere" -> arrayOf(1e9, 1e6, 1e3, 1.0)
            else -> arrayOf(1.0, 1e-3, 1e-6, 1e-9)
        }

        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    fun convertAngle(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "turn" -> arrayOf(1.0, 2 * Math.PI, 360.0, 400.0)
            "radian" -> arrayOf(1 / (2 * Math.PI), 1.0, 180 / Math.PI, 200 / Math.PI)
            "degree" -> arrayOf(1 / 360.0, 1 / 180.0, 1.0, 10 / 9.0)
            "gon" -> arrayOf(1 / 400.0, 1 / (200 / Math.PI), 9 / 10.0, 1.0)
            else -> arrayOf(1.0, 2 * Math.PI, 360.0, 400.0)
        }

        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    fun convertTemperature(unitType: String, value: Double): Array<String> {
        val results = Array(4) { "" }

        when (unitType) {
            "Celsius" -> {
                results[0] = value.toString()
                results[1] = (value * 9 / 5 + 32).toString()
                results[2] = (value + 273.15).toString()
                results[3] = (value * 33 / 100).toString()
            }
            "Fahrenheit" -> {
                results[0] = ((value - 32) * 5 / 9).toString()
                results[1] = value.toString()
                results[2] = ((value - 32) * 5 / 9 + 273.15).toString()
                results[3] = ((value - 32) * 5 / 9 * 33 / 100).toString()
            }
            "Kelvin" -> {
                results[0] = (value - 273.15).toString()
                results[1] = ((value - 273.15) * 9 / 5 + 32).toString()
                results[2] = value.toString()
                results[3] = ((value - 273.15) * 33 / 100).toString()
            }
            "Newton" -> {
                results[0] = (value * 100 / 33).toString()
                results[1] = ((value * 100 / 33) * 9 / 5 + 32).toString()
                results[2] = (value * 100 / 33 + 273.15).toString()
                results[3] = value.toString()
            }
            else -> {
                results[0] = value.toString()
                results[1] = (value * 9 / 5 + 32).toString()
                results[2] = (value + 273.15).toString()
                results[3] = (value * 33 / 100).toString()
            }
        }

        return results
    }

    fun convertVelocity(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "mph" -> arrayOf(1.0, 1.60934, 0.868976, 1.46667)
            "kmph" -> arrayOf(0.621371, 1.0, 0.539957, 0.911344)
            "knots" -> arrayOf(1.15078, 1.852, 1.0, 1.68781)
            "Foots per second" -> arrayOf(0.681818, 1.09728, 0.592484, 1.0)
            else -> arrayOf(1.0, 1.60934, 0.868976, 1.46667)
        }

        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    fun convertPressure(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "atmospheres" -> arrayOf(1.0, 101325.0, 1.01325, 760.0)
            "Pascals" -> arrayOf(9.86923e-6, 1.0, 1e-5, 0.00750062)
            "bars" -> arrayOf(0.986923, 100000.0, 1.0, 750.062)
            "torrs" -> arrayOf(0.00131579, 133.322, 0.00133322, 1.0)
            else -> arrayOf(1.0, 101325.0, 1.01325, 760.0)
        }

        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    fun convertLength(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "meters" -> arrayOf(1.0, 3.28084, 1.09361, 0.000621371)
            "foots" -> arrayOf(0.3048, 1.0, 0.333333, 0.000189394)
            "yards" -> arrayOf(0.9144, 3.0, 1.0, 0.000568182)
            "miles" -> arrayOf(1609.34, 5280.0, 1760.0, 1.0)
            else -> arrayOf(1.0, 3.28084, 1.09361, 0.000621371)
        }

        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    fun convertSurface(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "square inch" -> arrayOf(1.0, 0.00064516, 0.000000159423, 1.550e-10)
            "square foot" -> arrayOf(144.0, 1.0, 0.0000229568, 0.0000000229568)
            "square yard" -> arrayOf(9.0, 0.092903, 1.0, 0.000000000239006)
            "square mile" -> arrayOf(6.273e+7, 2.788e+7, 3.861e+6, 1.0)
            else -> arrayOf(1.0, 0.00064516, 0.000000159423, 1.550e-10)
        }

        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UnitConverterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UnitConverterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}