import React from 'react';
import DetailsScreen from './Details';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';
import { FontAwesome5 } from '@expo/vector-icons';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Svg, { Path } from 'react-native-svg'; 
import SvgUri from 'react-native-svg-uri';


const Stack = createStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="Home"
        screenOptions={{
          headerStyle: {
            backgroundColor: '#F6EDFF', // Kolor tła nagłówka
          },
          headerTintColor: 'black', // Kolor ikon i tekstu w nagłówku
          headerTitleStyle: {
            fontWeight: 'bold',
          },
        }}
      >
        <Stack.Screen
          name="Home"
          component={HomeScreen}
          options={{ headerShown: false }} // Ukryj domyślny nagłówek
        />
        <Stack.Screen
          name="Details"
          component={DetailsScreen}
          options={{
            header: ({ navigation }) => <CustomHeader navigation={navigation} />,
          }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const CustomHeader = ({ navigation }) => {
  return (
    <View style={styles.header}>
      <TouchableOpacity
        style={styles.backButton}
        onPress={() => navigation.goBack()}
      >
        <FontAwesome5 name="arrow-left" size={30} color="black" />
      </TouchableOpacity>
    </View>
  );
};

const HomeScreen = ({ navigation }) => {
  return (
    <View style={styles.container}>
      <View style={styles.gridContainer}>
        {renderButton("time", "clock", navigation, "Time")}
        {renderButton("mass", "weight-hanging", navigation, "Mass")}
        {renderButton("length", "ruler", navigation, "Length")}
        {renderButton("temperature", "thermometer-half", navigation, "Temperature")}
        {renderButton("amperage", "bolt", navigation, "Amperage")}
        {renderButton("velocity", "bolt", navigation, "Speed")}
        {renderButton("surface", "square", navigation, "Surface")}
        {renderButton("angle", "angle-right", navigation, "Angle")}
        {renderButton("pressure", "tachometer-alt", navigation, "Pressure")}
      </View>
    </View>
  );
};

const renderButton = (text, iconName, navigation, label) => (
  <TouchableOpacity
    style={styles.button}
    onPress={() => navigation.navigate('Details', { label })}
  >
        <SvgUri source={require(`./assets/icons/velocity.svg`)} />

    <Text>{text}</Text>
  </TouchableOpacity>
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F6EDFF',
    alignItems: 'center',
    justifyContent: 'center',
  },
  gridContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-around',
    marginTop: 20,
  },
  button: {
    width: 120,
    height: 120,
    backgroundColor: 'white',
    alignItems: 'center',
    justifyContent: 'center',
    margin: 5,
    borderRadius: 10,
  },
  header: {
    backgroundColor: '#F6EDFF', // Kolor tła nagłówka
    height: 100,
    flexDirection: 'row',
    alignItems: 'flex-end',
    paddingHorizontal: 10,
  },
  backButton: {
    marginRight: 10,
  },
});