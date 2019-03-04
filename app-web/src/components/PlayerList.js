import React, { Component } from "react";
import PlayerRegistration from "./PlayerRegistration";
import {ListGroup} from 'react-bootstrap'
import './PlayerList.css'

export default class extends Component {


  render() {
    const players = [...(this.props.players || [])].sort(
      (p1, p2) => p2.score - p1.score
    );

    const playerList = players.map(player =>
      <ListGroup.Item>{player.nickname}<span className="playerScore">{player.score}</span></ListGroup.Item>
    );

    return (
      <div className="playerList">
      <div className="playerRegistration">
        {this.props.myPlayer
          ? <div className="playerWelcome">{`Welcome, ${this.props.myPlayer.nickname}.`}</div>
          : this.props.isAdmin
              ? <div className="playerWelcome">{`You have all of the powers, Admin!`}</div>
              : <PlayerRegistration/>
        }
        <h2 className="title">Active Centrons</h2>

        <ListGroup>{playerList}</ListGroup>


        </div>
      </div>
    );
  }
}