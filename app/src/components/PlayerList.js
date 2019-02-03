import React, { Component } from "react";
import PlayerRegistration from "./PlayerRegistration";

export default class extends Component {


  render() {
    const players = this.props.players || [];

    const playerList = players.map(player =>
      <li key={player.id}>{player.nickname} ({player.score})</li>
    );

    return (
      <div>
        <h1>Registration</h1>
        <ul>{playerList}</ul>

        {this.props.myPlayer
          ? <h3>{`Welcome ${this.props.myPlayer.nickname}`}</h3>
          : <PlayerRegistration/>
        }
      </div>
    );
  }
}
