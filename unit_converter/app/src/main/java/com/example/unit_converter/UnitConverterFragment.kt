package com.example.unit_converter

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.android.material.textfield.TextInputEditText

class UnitConverterFragment : Fragment() {
    private val sharedViewModel: UnitTypeModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            "angle" -> arrayOf("degree", "radian", "turn", "gon")
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
        autoCompleteTextView?.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
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

    private fun saveConversionResults(results: Array<String>) {
        view?.findViewById<TextView>(R.id.resultUnitOne)?.text = results[0]
        view?.findViewById<TextView>(R.id.resultUnitTwo)?.text = results[1]
        view?.findViewById<TextView>(R.id.resultUnitThree)?.text = results[2]
        view?.findViewById<TextView>(R.id.resultUnitFour)?.text = results[3]
    }

    private fun calculateResults(value: Double, conversionFactors: Array<Double>): Array<String> {
        val results = Array(4) { "" }

        for (i in results.indices) {
            results[i] = (value * conversionFactors[i]).toString()
        }

        return results
    }

    private fun convertTime(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "seconds" -> arrayOf(1.0, 1 / 60.0, 1 / 3600.0, 1 / 604800.0)
            "minutes" -> arrayOf(60.0, 1.0, 1 / 60.0, 1 / 10800.0)
            "hours" -> arrayOf(3600.0, 60.0, 1.0, 1 / 144.0)
            "weeks" -> arrayOf(604800.0, 3600.0, 60.0, 1.0)
            else -> arrayOf(1.0, 1 / 60.0, 1 / 3600.0, 1 / 604800.0)
        }
        return calculateResults(value, conversionFactors)
    }

    private fun convertMass(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "milligram" -> arrayOf(1.0, 0.001, 1e-6, 1e-9)
            "gram" -> arrayOf(1000.0, 1.0, 0.001, 1e-6)
            "kilogram" -> arrayOf(1e6, 1000.0, 1.0, 0.001)
            "tonne" -> arrayOf(1e9, 1e6, 1000.0, 1.0)
            else -> arrayOf(1.0, 0.001, 1e-6, 1e-9)
        }
        return calculateResults(value, conversionFactors)
    }

    private fun convertAmperage(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "microampere" -> arrayOf(1.0, 1e-3, 1e-6, 1e-9)
            "milliampere" -> arrayOf(1e3, 1.0, 1e-3, 1e-6)
            "ampere" -> arrayOf(1e6, 1e3, 1.0, 1e-3)
            "kiloampere" -> arrayOf(1e9, 1e6, 1e3, 1.0)
            else -> arrayOf(1.0, 1e-3, 1e-6, 1e-9)
        }
        return calculateResults(value, conversionFactors)
    }

    private fun convertAngle(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "degree" -> arrayOf(1.0, Math.PI / 180.0, 0.002777777777777778, 1.1111111111111112)
            "radian" -> arrayOf(180.0 / Math.PI, 1.0, 0.159154943, 63.66)
            "turn" -> arrayOf(360.0, 6.28318531, 1.0, 400.0)
            "gon" -> arrayOf(0.9, 0.0157079633, 0.0025, 1.0)
            else -> arrayOf(1.0, Math.PI / 180.0, 0.0027, 1.11)
        }
        return calculateResults(value, conversionFactors)
    }

    private fun convertTemperature(unitType: String, value: Double): Array<String> {
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

    private fun convertVelocity(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "mph" -> arrayOf(1.0, 1.60934, 0.868976, 1.46667)
            "kmph" -> arrayOf(0.621371, 1.0, 0.539957, 0.911344)
            "knots" -> arrayOf(1.15078, 1.852, 1.0, 1.68781)
            "Foots per second" -> arrayOf(0.681818, 1.09728, 0.592484, 1.0)
            else -> arrayOf(1.0, 1.60934, 0.868976, 1.46667)
        }
        return calculateResults(value, conversionFactors)
    }

    private fun convertPressure(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "atmospheres" -> arrayOf(1.0, 101325.0, 1.01325, 760.0)
            "Pascals" -> arrayOf(9.86923e-6, 1.0, 1e-5, 0.00750062)
            "bars" -> arrayOf(0.986923, 100000.0, 1.0, 750.062)
            "torrs" -> arrayOf(0.00131579, 133.322, 0.00133322, 1.0)
            else -> arrayOf(1.0, 101325.0, 1.01325, 760.0)
        }
        return calculateResults(value, conversionFactors)
    }

    private fun convertLength(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "meters" -> arrayOf(1.0, 3.28084, 1.09361, 0.000621371)
            "foots" -> arrayOf(0.3048, 1.0, 0.333333, 0.000189394)
            "yards" -> arrayOf(0.9144, 3.0, 1.0, 0.000568182)
            "miles" -> arrayOf(1609.34, 5280.0, 1760.0, 1.0)
            else -> arrayOf(1.0, 3.28084, 1.09361, 0.000621371)
        }
        return calculateResults(value, conversionFactors)
    }

    private fun convertSurface(unitType: String, value: Double): Array<String> {
        val conversionFactors = when (unitType) {
            "square inch" -> arrayOf(1.0, 0.00064516, 0.000000159423, 1.550e-10)
            "square foot" -> arrayOf(144.0, 1.0, 0.0000229568, 0.0000000229568)
            "square yard" -> arrayOf(9.0, 0.092903, 1.0, 0.000000000239006)
            "square mile" -> arrayOf(6.273e+7, 2.788e+7, 3.861e+6, 1.0)
            else -> arrayOf(1.0, 0.00064516, 0.000000159423, 1.550e-10)
        }
        return calculateResults(value, conversionFactors)
    }

}