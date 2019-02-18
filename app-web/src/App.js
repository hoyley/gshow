import React, { Component } from 'react';
import './App.css';
import Screen from './components/Screen';
import service from './service/GshowService';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = { appState: service.appState };
  }

  componentDidMount() {
    service.appState.setChangeHandler(() => this.onAppStateChange());
  }

  onAppStateChange() {
    this.setState({ appState: service.appState });
  }

  render() {
    return (
      <Screen {...this.state.appState} />
    );
  }
}

export default App;
