import React, { useState } from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { TextInput, Menu, Provider, List, Button, TouchableOppacity } from 'react-native-paper';
import Icon from 'react-native-vector-icons/MaterialIcons';
import AntIcon from "react-native-vector-icons/AntDesign";

const DetailsScreen = ({ route }) => {
  const { label } = route.params;
  const [unitType, setUnitType] = useState('');
  const [numberInput, setNumberInput] = useState('');
  const [visible, setVisible] = useState(false);
  const [expanded, setExpanded] = useState(false);

  const openMenu = () => setVisible(true);
  const closeMenu = () => setVisible(false);
  const handlePress = () => setExpanded(!expanded);
  const dropdownItems = getDropdownItems(label.toLowerCase());

  const handleUnitTypeChange = (value) => {
    setUnitType(value);
    closeMenu();
  };

  const handleNumberInputChange = (text) => {
    if (/^\d+$/.test(text) || text === '') {
      setNumberInput(text);
    }
  };

  return (
    <Provider>
      <View style={styles.container}>
        <Menu
          visible={visible}
          onDismiss={closeMenu}
          style={styles.menu}
          onT
          anchor={
            <View style={styles.dropdown} > 
              <TextInput
                label="unit"
                value={unitType}
                onTouchStart={openMenu}
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

        <TextInput
          style={styles.textInput}
          label="value"
          keyboardType="numeric"
          value={numberInput}
          onChangeText={handleNumberInputChange}
          mode='outlined'
        />

      </View>
    </Provider>
  );
};

const getDropdownItems = (iconType) => {
  const dropdownItemsMap = {
    time: ["seconds", "minutes", "hours", "weeks"],
    mass: ["milligram", "gram", "kilogram", "tonne"],
    amperage: ["microampere", "milliampere", "ampere", "kiloampere"],
    angle: ["turn", "radian", "degree", "gon"],
    temperature: ["Celsius", "Fahrenheit", "Kelvin", "Newton"],
    velocity: ["mph", "kmph", "knots", "Foots per second"],
    pressure: ["atmospheres", "Pascals", "bars", "torrs"],
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
    justifyContent: 'center',
  },
  textInput: {
    width: '77%',
    height: 60,
    marginTop: 10,
    paddingHorizontal: 5,
    backgroundColor: 'white',
  },
  dropdown: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingRight: 10,
    width: 300,
    marginTop: 10,
    backgroundColor: 'white',
    borderBottomWidth: 0.5

  },
  innerDropdown: {
    backgroundColor: 'white',
  },
  menu: {
    borderBottomWidth: 0.5
  }

});

export default DetailsScreen;