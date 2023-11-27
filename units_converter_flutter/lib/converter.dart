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
  List<String> values = [];
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
    List<String> updatedValues = [];

    String baseUnit = getDropdownItems(widget.resultName).first;
    if(dropdownValue != null) {
      baseUnit = dropdownValue;
    }

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
      "time": {"seconds": 1, "minutes": 60, "hours": 3600, "weeks": 604800},
      "mass": {"milligram": 0.001, "gram": 1, "kilogram": 1000, "tonne": 1000000},
      "amperage": {"microampere": 0.000001, "milliampere": 0.001, "ampere": 1, "kiloampere": 1000},
      "angle": {"turn": 360, "radian": 57.2958, "degree": 1, "gon": 1.11111},
      "temperature": {"Celsius": 1, "Fahrenheit": 33.8, "Kelvin": 274.15, "Newton": 0.33},
      "velocity": {"mph": 0.44704, "kmph": 0.277778, "knots": 0.514444, "Foots per second": 0.3048},
      "pressure": {"atmospheres": 101325, "Pascals": 1, "bars": 100000, "torrs": 133.322},
      "length": {"meters": 1, "foots": 0.3048, "yards": 0.9144, "miles": 1609.34},
      "surface": {
        "square inch": 0.00064516,
        "square foot": 0.092903,
        "square yard": 0.836127,
        "square mile": 2589988.11
      },
    };

    double baseValue = value / conversionMap[widget.resultName]![fromUnit]!;
    return baseValue * conversionMap[widget.resultName]![toUnit]!;
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
        iconTheme: IconThemeData(size: 40),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SvgPicture.asset(
              iconPath,
              width: 40,
              height: 40,
            ),
            SizedBox(width: 8),
            Text(
              widget.resultName,
              style: TextStyle(
                fontSize: 24,
              ),
            ),
          ],
        ),
        toolbarHeight: 120.0,
        backgroundColor: Color(0xFFF6EDFF),
      ),
      body: Container(
        color: Color(0xFFF6EDFF),
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              DropdownButton<String>(
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
                hint: Text('Select an option'),
              ),
              SizedBox(height: 16),
              TextField(
                controller: textEditingController,
                decoration: InputDecoration(
                  labelText: 'value',
                  border: OutlineInputBorder(),
                ),
                keyboardType: TextInputType.numberWithOptions(decimal: true),
                inputFormatters: [FilteringTextInputFormatter.allow(RegExp(r'^\d*\.?\d*'))],
              ),
              SizedBox(height: 16),
              Container(
                color: Color(0xFFE2DBF6),
                padding: EdgeInsets.all(8),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: List.generate(
                    getDropdownItems(widget.resultName).length,
                        (index) => Row(
                      children: [
                        Expanded(
                          child: Text(
                            values.isNotEmpty ? values[index] : '',
                            textAlign: TextAlign.right,
                            style: TextStyle(fontSize: 16),
                          ),
                        ),
                        SizedBox(width: 8),
                        Expanded(
                          child: Text(
                            getDropdownItems(widget.resultName)[index],
                            textAlign: TextAlign.left,
                            style: TextStyle(fontSize: 16),
                          ),
                        ),
                      ],
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