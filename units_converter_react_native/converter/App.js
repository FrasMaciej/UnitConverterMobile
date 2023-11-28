import React from 'react';
import DetailsScreen from './Details';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';
import { FontAwesome5 } from '@expo/vector-icons';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Amperage from './assets/icons/amperage.svg';
import Angle from './assets/icons/angle.svg';
import Length from './assets/icons/length.svg';
import Mass from './assets/icons/mass.svg';
import Pressure from './assets/icons/pressure.svg';
import Surface from './assets/icons/surface.svg';
import Temperature from './assets/icons/temperature.svg';
import Time from './assets/icons/time.svg';
import Velocity from './assets/icons/velocity.svg';

const iconMapping = {
  amperage: <Amperage />,
  angle: <Angle />,
  length: <Length />,
  mass: <Mass />,
  pressure: <Pressure />,
  surface: <Surface />,
  temperature: <Temperature />,
  time: <Time />,
  velocity: <Velocity />,
};

const Stack = createStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="Home"
        screenOptions={{
          headerStyle: {
            backgroundColor: '#F6EDFF',
          },
          headerTintColor: 'black', 
          headerTitleStyle: {
            fontWeight: 'bold',
          },
        }}
      >
        <Stack.Screen
          name="Home"
          component={HomeScreen}
          options={{ headerShown: false }} 
        />
        <Stack.Screen
          name="Details"
          component={DetailsScreen}
          options={({ route }) => ({
            header: ({ navigation }) => (
              <CustomHeader
                navigation={navigation}
                icon={iconMapping[route.params.iconName.toLowerCase()]}
                iconName={route.params.iconName.toLowerCase()}
              />
            ),
          })}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const CustomHeader = ({ navigation, icon, iconName }) => {
  return (
    <View style={styles.header}>
      <TouchableOpacity
        style={styles.backButton}
        onPress={() => navigation.goBack()}>
        <FontAwesome5 name="arrow-left" size={30} color="black" />
      </TouchableOpacity>
      <View style={styles.headerContent}>
        {icon}
        <Text style={styles.iconNameText}>{iconName}</Text>
      </View>
    </View>
  );
};

const HomeScreen = ({ navigation }) => {
  return (
    <View style={styles.container}>
      <View style={styles.gridContainer}>
        {renderButton("time", "Time", navigation, "Time")}
        {renderButton("mass", "Mass", navigation, "Mass")}
        {renderButton("length", "Length", navigation, "Length")}
        {renderButton("temperature", "Temperature", navigation, "Temperature")}
        {renderButton("amperage", "Amperage", navigation, "Amperage")}
        {renderButton("velocity", "Velocity", navigation, "Velocity")}
        {renderButton("surface", "Surface", navigation, "Surface")}
        {renderButton("angle", "Angle", navigation, "Angle")}
        {renderButton("pressure", "Pressure", navigation, "Pressure")}
      </View>
    </View>
  );
};

const renderButton = (text, iconName, navigation, label) => (
  <TouchableOpacity
    style={styles.button}
    onPress={() => navigation.navigate('Details', { label, iconName })}
  >
    {iconMapping[iconName.toLowerCase()]}
    <Text>{text}</Text>
  </TouchableOpacity>
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F6EDFF',
    marginTop: 50
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
    backgroundColor: '#F6EDFF',
    height: 75,
    flexDirection: 'row',
    alignItems: 'flex-end',
    paddingHorizontal: 10,
    marginTop: 50

  },
  backButton: {
    marginRight: 10,
  },
  headerContent: {
    flex: 1, 
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center', 
  },
  iconNameText: {
    marginLeft: 8,
    fontSize: 18,
    fontWeight: 'bold',
  },
});