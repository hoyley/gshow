import React from "react";
import PlayerRegistration from "./PlayerRegistration";

export default (props) => {
  const players = props.players || [];

  const playerList = players.map(player =>
    <li key={player.id}>{player.nickname}</li>
  );

  return (
    <div>
      <h1>Registration</h1>
      <ul>{playerList}</ul>

      {props.myPlayer
        ? <h3>{"Welcome " + props.myPlayer.nickname}</h3>
        : <PlayerRegistration/>
      }
    </div>
  );
}
