import React from "react";

export default (props) => {
  return <ul>
    <li>{ "Remaining Time: " + props.remainingTime}</li>
    <li>{ "Remaining Points: " + props.remainingPoints}</li>
    <li>{ "Game Over: " + props.gameOver}</li>
  </ul>
}
