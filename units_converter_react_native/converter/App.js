import React, { useState } from 'react';
import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';
import { FontAwesome5 } from '@expo/vector-icons';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { Searchbar } from 'react-native-paper'; 
import Amperage from './assets/icons/amperage.svg';
import Angle from './assets/icons/angle.svg';
import Length from './assets/icons/length.svg';
import Mass from './assets/icons/mass.svg';
import Pressure from './assets/icons/pressure.svg';
import Surface from './assets/icons/surface.svg';
import Temperature from './assets/icons/temperature.svg';
import Time from './assets/icons/time.svg';
import Velocity from './assets/icons/velocity.svg';
import DetailsScreen from './Details';

const iconMapping = {
  time: <Time />,
  mass: <Mass />,
  length: <Length />,
  temperature: <Temperature />,
  amperage: <Amperage />,
  velocity: <Velocity />,
  surface: <Surface />,
  angle: <Angle />,
  pressure: <Pressure />,
};

const Stack = createStackNavigator();

export default function App() {
  const [searchQuery, setSearchQuery] = useState('');

  const onChangeSearch = (query) => setSearchQuery(query);

  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="Home"
        screenOptions={{
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
                searchQuery={searchQuery}
                onChangeSearch={onChangeSearch}
              />
            ),
          })}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const CustomHeader = ({ navigation, icon, iconName, searchQuery, onChangeSearch }) => {
  return (
    <View style={styles.header}>
      <TouchableOpacity
        style={styles.backButton}
        onPress={() => navigation.goBack()}
      >
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
  const [searchQuery, setSearchQuery] = useState('');

  const onChangeSearch = (query) => setSearchQuery(query);

  const filteredButtons = Object.keys(iconMapping).filter((iconName) =>
  iconName.toLowerCase().includes(searchQuery.toLowerCase())
);

  return (
    <View style={styles.homeScreenContainer}>

      <View style={styles.container}>
      <Searchbar
        placeholder="Search"
        onChangeText={onChangeSearch}
        value={searchQuery}
        style={styles.searchBar}
        elevation={5}      
        clearIcon={false}
        trailingIconColor={'black'}
        mode={"bar"}/>
        <View style={styles.gridContainer}>
          {filteredButtons.map((iconName) =>
            renderButton(iconName, iconName, navigation, iconName)
          )}
        </View>
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
    flexDirection: 'column', 
  },
  gridContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
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
  searchBar: {
    marginTop: 50,
    marginRight: 10,
    marginLeft: 10,
    backgroundColor: '#FFFFFF',
  },
  homeScreenContainer: {
    flex: 1,
    backgroundColor: '#F6EDFF',
  },
});