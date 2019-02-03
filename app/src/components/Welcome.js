import React, { Component } from "react";
import facade from "../api/Facade";

export default class extends Component {

  startGame() {
    facade.startGame();
  }

  render() {
    return <div>
      <h1>Welcome</h1>
      <button onClick={() => this.startGame()}>Start Game!</button>
    </div>;
  }
}
