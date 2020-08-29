import React, { Component } from "react";
import PlayerRegistration from "./PlayerRegistration";
import {ListGroup} from 'react-bootstrap'
import './PlayerList.css'

export default class extends Component {

  getPlayerClass(player) {
    let playerClass = "playerName";
    if (!!this.props.myPlayer &&
        player.nickname === this.props.myPlayer.nickname) {
      playerClass += " playerIsMe";
    }
    return playerClass;
  }

  render() {
    const players = [...(this.props.players || [])].sort(
      (p1, p2) => p2.score - p1.score
    );

    const playerList = players.map(player =>
      <ListGroup.Item>
        <label className={this.getPlayerClass(player)}>{player.nickname}</label>
        <span className="playerScore">{player.score}</span>
      </ListGroup.Item>
    );

    return (
      <div className="playerList">
        {
          !this.props.isAdmin && !this.props.myPlayer &&
            <PlayerRegistration/>
        }
        {
          players.length > 0 &&
            <div>
              <h2 className="title">Active Players</h2>
              <ListGroup>{playerList}</ListGroup>
            </div>
        }
      </div>
    );
  }
}
