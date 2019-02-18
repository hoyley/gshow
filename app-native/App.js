import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import service from './service/GshowService'
import Screen from './components/Screen.js'

export default class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = { appState: service.appState };
    service.appState.setChangeHandler(() => this.onAppStateChange());
  }

  onAppStateChange() {
    this.setState({ appState: service.appState });
  }
  
  render() {
    return (
      <View style={styles.container}>
        <Screen appState={this.state.appState} />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
