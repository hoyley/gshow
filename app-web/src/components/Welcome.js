import React, { Component } from "react";
import service from "../service/GshowService";
import './Welcome.css';
import FancyButton from './controls/FancyButton.js';

export default class extends Component {

  startGame() {
    service.startGame();
  }

  render() {
    return <div className="welcomeScreen">
      <div>
        <FancyButton className="welcomeButton"
                     buttonText="Start Game!"
                     onClick={() => this.startGame()} />
      </div>
    </div>;
  }
}
