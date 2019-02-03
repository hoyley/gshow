import React, { Component } from 'react';
import './App.css';
import Screen from './components/Screen';
import AppState from './AppState.js';

class App extends Component {

  constructor(props) {
    super(props);
    this.appState = new AppState(() => this.onAppStateChange());
    this.state = { appState: this.appState };
  }

  componentDidMount() {
    this.appState.start();
  }
  
  onAppStateChange() {
    this.setState({ appState: this.appState });
  }

  render() {
    return (
      <Screen {...this.state.appState} />
    );
  }
}

export default App;
