import React from "react";
import WelcomeScreen from "./Welcome";
import PlayerList from "./PlayerList";
import ChoiceGame from "./ChoiceGame";
import Error from "./Error";
import './Screen.css';

const getMainScreen = (props) => {
  if (props.screen === "Welcome") {
    return <WelcomeScreen />;
  } else if (props.screen === "ChoiceGame") {
    return <ChoiceGame gameStatus={props.gameStatus} gameConfig={props.gameConfig} players={props.players}
                       myPlayer={props.myPlayer} />;
  } else {
    return <Error msg={"Unknown screen: " + props.screen} />
  }
}

export default (props) => {
  return <div className="screen">
    <div className="playerList"><PlayerList players={props.players} myPlayer={props.myPlayer} /></div>
    <div className="gameArea">{ getMainScreen(props) }</div>
  </div>
}
