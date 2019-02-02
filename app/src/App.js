import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import facade from './api/Facade.js'

class App extends Component {

    componentDidMount() {
        facade.employee(1).done(response => {
            debugger;
            this.setState({employees: response});
        });
    }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            Edit <code>src/App.js</code> and save to reload.
          </p>
          <a
            className="App-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React
          </a>
        </header>
      </div>
    );
  }
}

export default App;
