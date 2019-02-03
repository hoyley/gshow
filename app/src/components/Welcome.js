import React, { Component } from "react";
import facade from "../api/Facade";
import './Welcome.css';
import FancyButton from './FancyButton.js';

export default class extends Component {

  startGame() {
    facade.startGame();
  }

  render() {
    return <div className="welcome">
      <div>
        <FancyButton buttonText="Start Game!" onClick={() => this.startGame()} />
      </div>
    </div>;
  }
}
