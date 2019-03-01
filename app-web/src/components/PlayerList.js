import React, { Component } from "react";
import PlayerRegistration from "./PlayerRegistration";
import {ListGroup} from 'react-bootstrap'
import './PlayerList.css'

export default class extends Component {


  render() {
    const players = this.props.players || [];

    const playerList = players.map(player =>
      <ListGroup.Item>{player.nickname}<span class="playerScore">{player.score}</span></ListGroup.Item>
    );

    return (
      <div className="playerList">
        <h2 className="title">Active Players</h2>
        <ListGroup>{playerList}</ListGroup>

        <div className="playerRegistration">
          {this.props.myPlayer
            ? <div>{`Welcome ${this.props.myPlayer.nickname}`}</div>
            : <PlayerRegistration/>
          }
        </div>
      </div>
    );
  }
}
