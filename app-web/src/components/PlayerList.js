import React, { Component } from "react";
import PlayerRegistration from "./PlayerRegistration";
import './PlayerList.css'

export default class extends Component {


  render() {
    const players = this.props.players || [];

    const playerList = players.map(player =>
      <li key={player.id}>{player.nickname} ({player.score})</li>
    );

    return (
      <div className="playerList">
        <h1>Registration</h1>
        <ul>{playerList}</ul>

        {this.props.myPlayer
          ? <div>{`Welcome ${this.props.myPlayer.nickname}`}</div>
          : <PlayerRegistration/>
        }
      </div>
    );
  }
}
