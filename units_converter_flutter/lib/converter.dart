import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';

class Converter extends StatefulWidget {
  final String resultName;

  const Converter({Key? key, required this.resultName}) : super(key: key);

  @override
  _ConverterState createState() => _ConverterState();
}

class _ConverterState extends State<Converter> {
  String iconPath = '';
  String dropdownValue = '';
  List<String> values = ["0.0", "0.0", "0.0", "0.0"];
  TextEditingController textEditingController = TextEditingController();

  @override
  void initState() {
    super.initState();
    iconPath = 'assets/icons/${widget.resultName}.svg';
    dropdownValue = getDropdownItems(widget.resultName).first;
    textEditingController.addListener(updateConvertedValues);
  }

  void updateConvertedValues() {
    double inputValue = double.tryParse(textEditingController.text) ?? 0;
    String baseUnit = getDropdownItems(widget.resultName).first;

    baseUnit = dropdownValue;

    List<String> updatedValues = [];

    for (String unit in getDropdownItems(widget.resultName)) {
      double convertedValue = convert(baseUnit, unit, inputValue);
      updatedValues.add(convertedValue.toString());
    }

    setState(() {
      values = updatedValues;
    });
  }

  double convert(String fromUnit, String toUnit, double value) {
    Map<String, Map<String, double>> conversionMap = {
      "time": {"seconds": 1, "minutes": 1/60, "hours": 1/3600, "weeks": 1/604800},
      "mass": {"milligram": 1, "gram": 0.001, "kilogram": 1e-6, "tonne": 1e-9},
      "amperage": {
        "microampere": 1,
        "milliampere": 1e-3,
        "ampere": 1e-6,
        "kiloampere": 1e-9
      },
      "angle": {"degree": 1, "radian": 0.01745329252, "turn": 0.0027,  "gon": 1.11111},
      "temperature": {"Celsius": 1, "Fahrenheit": 33.8, "Kelvin": 274.15, "Newton": 0.33},
      "velocity": {
        "mph": 1,
        "kmph": 1.60934,
        "knots": 0.868976,
        "Foots per second": 1.46667
      },
      "pressure": {"atmospheres": 1, "Pascals": 101325.0, "bars": 1.01325, "torrs": 760.0},
      "length": {"meters": 1, "foots": 3.28084, "yards": 1.09361, "miles": 0.000621371},
      "surface": {
        "square inch": 1,
        "square foot": 1/144.0,
        "square yard": 1/9.0,
        "square mile": 1/4014489599
      },
    };

    double baseValue = value / conversionMap[widget.resultName]![fromUnit]!;
    double resultValue = baseValue * conversionMap[widget.resultName]![toUnit]!;
    return resultValue;
  }

  List<String> getDropdownItems(String iconType) {
    Map<String, List<String>> dropdownItemsMap = {
      "time": ["seconds", "minutes", "hours", "weeks"],
      "mass": ["milligram", "gram", "kilogram", "tonne"],
      "amperage": ["microampere", "milliampere", "ampere", "kiloampere"],
      "angle": ["turn", "radian", "degree", "gon"],
      "temperature": ["Celsius", "Fahrenheit", "Kelvin", "Newton"],
      "velocity": ["mph", "kmph", "knots", "Foots per second"],
      "pressure": ["atmospheres", "Pascals", "bars", "torrs"],
      "length": ["meters", "foots", "yards", "miles"],
      "surface": ["square inch", "square foot", "square yard", "square mile"],
    };

    return dropdownItemsMap[iconType] ?? ["seconds", "minutes", "hours", "weeks"];
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: true,
        iconTheme: const IconThemeData(size: 40),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SvgPicture.asset(
              iconPath,
              width: 40,
              height: 40,
            ),
            const SizedBox(width: 8),
            Text(
              widget.resultName,
              style: const TextStyle(
                fontSize: 24,
              ),
            ),
          ],
        ),
        toolbarHeight: 120.0,
        backgroundColor: const Color(0xFFF6EDFF),
      ),
      body: Container(
        color: const Color(0xFFF6EDFF),
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Row(
                children: [
                  Expanded(
                    child: Container(
                      child: DropdownButtonHideUnderline(
                        child: InputDecorator(
                          decoration: const InputDecoration(
                            contentPadding:
                            EdgeInsets.symmetric(horizontal: 10.0, vertical: 4.0),
                            filled: true,
                            fillColor: Colors.white,
                            labelText: 'From',
                            border: UnderlineInputBorder(
                              borderSide: BorderSide(color: Colors.grey),
                            ),
                          ),
                          child: DropdownButton<String>(
                            items: getDropdownItems(widget.resultName)
                                .map((String value) => DropdownMenuItem<String>(
                              value: value,
                              child: Text(value),
                            ))
                                .toList(),
                            onChanged: (String? newValue) {
                              if (newValue != null) {
                                setState(() {
                                  dropdownValue = newValue;
                                  updateConvertedValues();
                                });
                              }
                            },
                            value: dropdownValue,
                          ),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: TextField(
                      controller: textEditingController,
                      decoration: const InputDecoration(
                        labelText: 'Value',
                        border: OutlineInputBorder(),
                        filled: true,
                        fillColor: Colors.white,
                      ),
                      keyboardType:
                      const TextInputType.numberWithOptions(decimal: true),
                      inputFormatters: [
                        FilteringTextInputFormatter.allow(RegExp(r'^\d*\.?\d*'))
                      ],
                      style: const TextStyle(
                        height: 2.2,
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              Container(
                margin: const EdgeInsets.only(top: 40.0),
                color: const Color(0xFFEADDFF),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: List.generate(
                    getDropdownItems(widget.resultName).length,
                        (index) => Container(
                      height: 50,
                      child: Row(
                        children: [
                          Expanded(
                            child: Text(
                              values.isNotEmpty ? values[index] : '',
                              textAlign: TextAlign.right,
                              style: const TextStyle(
                                  fontSize: 16, color: Color(0xFFA7310C)),
                            ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: Text(
                              getDropdownItems(widget.resultName)[index],
                              textAlign: TextAlign.left,
                              style: const TextStyle(fontSize: 16),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
