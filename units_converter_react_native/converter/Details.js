import React, { useState, useEffect } from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { TextInput, Menu, Provider } from 'react-native-paper';
import AntIcon from "react-native-vector-icons/AntDesign";

const DetailsScreen = ({ route }) => {
  const { label } = route.params;
  const [unitType, setUnitType] = useState('');
  const [numberInput, setNumberInput] = useState('');
  const [visible, setVisible] = useState(false);
  const [convertedResults, setConvertedResults] = useState({
    seconds: 0,
    minutes: 0,
    hours: 0,
    weeks: 0,
  });
  const openMenu = () => setVisible(true);
  const closeMenu = () => setVisible(false);
  const dropdownItems = getDropdownItems(label.toLowerCase());

  const handleUnitTypeChange = (value) => {
    setUnitType(value);
    convertUnits(value, numberInput);
    closeMenu();
  };

  const handleNumberInputChange = (text) => {
    if (/^\d*\.?\d*$/.test(text)) {
      setNumberInput(text);
      convertUnits(unitType, text);
    }
  };

  const convertUnits = (unit, value) => {

    const isNotTemperature = unit!=="celsius" && unit!=="fahrenheit" && unit!=="kelvin" && unit!=="newton"
    let basicValue = 0
    if(unit==="celsius") {
      basicValue = value;
    } else if(unit==="fahrenheit") {
      basicValue = (value - 32) * (5/9)
    } else if(unit==="kelvin") {
      basicValue = value - 273.15
    } else if(unit==="newton") {
      basicValue = value * (100/33)
    }

    const conversionFactorsMap = {
      time: { 'seconds': 1, 'minutes': 1/60, 'hours': 1/3600, 'weeks': 1/604800 },
      mass: { 'milligram': 1, 'gram': 0.001, 'kilogram': 1e-6, 'tonne': 1e-9 },
      amperage: { 'microampere': 1, 'milliampere': 1e-3, 'ampere': 1e-6, 'kiloampere': 1e-9 },
      angle: { 'degree': 1, 'radian': 0.01745329252, 'turn': 0.0027, 'gon': 1.11 },
      temperature: { 'celsius': basicValue, 'fahrenheit':(Number(basicValue) * ( 9 / 5) + 32), 'kelvin': (Number(basicValue) + 273.15), 'newton': Number(basicValue) * 33 / 100},
      velocity: { 'mph': 1, 'kmph': 1.60934, 'knots': 0.868976, 'fps': 1.46667 },
      pressure: { 'atmospheres': 1, 'pascals': 101325.0, 'bars': 1.01325, 'torrs': 760.0 },
      length: { 'meters': 1, 'foots': 3.28084, 'yards': 1.09361, 'miles': 0.000621371 },
      surface: { 'square inch': 1, 'square foot': 1/144.0, 'square yard': 1/9.0, 'square mile': 1/4014489599 },
    };

    const conversionFactors = conversionFactorsMap[label.toLowerCase()] || {};
    const convertedResults = {};


    if(isNotTemperature) {
      for (const targetUnit in conversionFactors) {
        if(targetUnit === unit ){ basicValue = (1.0 / conversionFactors[targetUnit]) * value;}
      }
    }

    for (const targetUnit in conversionFactors) {
      const factor = conversionFactors[targetUnit];
      const result = isNotTemperature ? parseFloat(basicValue) * factor : factor
      convertedResults[targetUnit] = result ? result : 0.0;
    }

    setConvertedResults(convertedResults);
  };

  useEffect(() => {
    const defaultUnit = getDropdownItems(label.toLowerCase())[0];
    setUnitType(defaultUnit);
  
    const defaultConvertedResults = {};
    getDropdownItems(label.toLowerCase()).forEach((unit) => {
      defaultConvertedResults[unit] = 0;
    });
    convertUnits(defaultUnit, numberInput);

  }, [label]);

  return (
    <Provider>
      <View style={styles.container}>
        <View style={styles.inputRow}>
          <View onTouchStart={openMenu}>
            <Menu
              visible={visible}
              onDismiss={closeMenu}
              style={styles.menu}
              anchor={
                <View style={styles.dropdown}>
                  <TextInput
                    label="From"
                    value={unitType}
                    style={styles.innerDropdown}
                    caretHidden={true}
                    showSoftInputOnFocus={false}
                    underlineColor="transparent"
                  />
                  <AntIcon
                    name={visible ? 'caretup' : 'caretdown'}
                    size={10}
                    color="#000"
                  />
                </View>
              }
            >
              {dropdownItems.map((item, index) => (
                <Menu.Item
                  key={index}
                  onPress={() => handleUnitTypeChange(item)}
                  title={item.charAt(0).toUpperCase() + item.slice(1)}
                  style={{ maxWidth: '100%' }}
                />
              ))}
            </Menu>
          </View>

          <TextInput
            style={styles.textInput}
            label="Value"
            keyboardType="numeric"
            value={numberInput}
            onChangeText={handleNumberInputChange}
            mode='outlined'
          />
        </View>

        <View style={styles.resultsContainer}>
          {Object.entries(convertedResults).map(([unit, value]) => (
            <View key={unit} style={styles.resultItemContainer}>
              <Text style={styles.resultItemValue}>{value}</Text>
              <Text style={styles.resultItemUnit}>{unit}</Text>
            </View>
          ))}
        </View>
      </View>
    </Provider>
  );
};

const getDropdownItems = (iconType) => {
  const dropdownItemsMap = {
    time: ["seconds", "minutes", "hours", "weeks"],
    mass: ["milligram", "gram", "kilogram", "tonne"],
    amperage: ["microampere", "milliampere", "ampere", "kiloampere"],
    angle: ["degree", "radian", "gon", "turn"],
    temperature: ["celsius", "fahrenheit", "kelvin", "newton"],
    velocity: ["mph", "kmph", "knots", "fps"],
    pressure: ["atmospheres", "pascals", "bars", "torrs"],
    length: ["meters", "foots", "yards", "miles"],
    surface: ["square inch", "square foot", "square yard", "square mile"],
  };

  return dropdownItemsMap[iconType] || [];
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F6EDFF',
    alignItems: 'center',
    paddingTop: 40
  },
  textInput: {
    height: 60,
    backgroundColor: 'white',
    width: 175,
    height: 75
  },
  dropdown: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingRight: 10,
    marginTop: 6,
    backgroundColor: 'white',
    borderBottomWidth: 0.5,
    width: 175,
    height: 75
  },
  innerDropdown: {
    backgroundColor: 'white',
  },
  inputRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 10
  },
  resultItemContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 6,
    height: 30,
  },
  resultItemValue: {
    flex: 1,
    textAlign: 'right',
    fontSize: 16,
    color: '#A7310C'
  },
  resultItemUnit: {
    flex: 1,
    textAlign: 'left',
    fontSize: 16
  },
  resultsContainer: {
    marginTop: 60,
    backgroundColor: '#EADDFF',
    padding: 10,
    borderRadius: 8,
    width: '100%',
    flexDirection: 'column',
    justifyContent: 'space-around',
    gap: 10,
  },
});

export default DetailsScreen;
