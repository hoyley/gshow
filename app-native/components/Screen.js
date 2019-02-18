import React from "react";
import WelcomeScreen from "./Welcome";
import Error from "./Error";
import { StyleSheet, Text, View } from 'react-native';
import ChoiceGame from './ChoiceGame';

const getMainScreen = (props) => {
  let screen = props.appState && props.appState.screen;
  console.log(props.appState.screen);

  if (screen === "Welcome") {
    return <WelcomeScreen />;
  } else if (screen === "ChoiceGame") {
    return <ChoiceGame appState={props.appState} />
  } else {
    return <Error msg={"Unknown screen: " + screen} />
  }
}

export default (props) => {
  return <View>
    {getMainScreen(props) }
    </View>
}
