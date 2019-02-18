import React, { Component } from "react";
import PlayerRegistration from "./PlayerRegistration";
import {ListGroup} from 'react-bootstrap'
import './PlayerList.css'

export default class extends Component {


  render() {
    const players = this.props.players || [];

    const playerList = players.map(player =>
      <ListGroup.Item className="playerListItem">{player.nickname} ({player.score})</ListGroup.Item>
    );

    return (
      <div className="playerList">
        <h1>Registration</h1>
        <ListGroup>{playerList}</ListGroup>

        {this.props.myPlayer
          ? <div>{`Welcome ${this.props.myPlayer.nickname}`}</div>
          : <PlayerRegistration/>
        }
      </div>
    );
  }
}
