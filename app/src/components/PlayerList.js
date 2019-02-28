import React, { Component } from "react";
import PlayerRegistration from "./PlayerRegistration";
import './PlayerList.css'

export default class extends Component {


  render() {
    const players = this.props.players || [];

    const playerList =
    players.map(player =>
      <li key={player.id}>{player.nickname}<span class="playerScore">{player.score}</span></li>
    );

    return (
      <div>
      {this.props.myPlayer
        ? <div>{`Welcome, ${this.props.myPlayer.nickname}.`}</div>
        : <PlayerRegistration/>
      }
        <h2 class="title">Active Players</h2>
        <ul>{playerList}</ul>
      </div>
    );
  }
}
