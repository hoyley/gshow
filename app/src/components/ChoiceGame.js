import React from "react";
import ChoiceGameOptions from './ChoiceGameOptions';
import ChoiceGameAnswer from './ChoiceGameAnswer';


const getMyAnswer = (props) => {
  return props.gameConfig.status.gameOver && props.gameConfig.playerAnswers.find(p => p.id === props.myPlayer.id);
};

export default (props) => {

  return <div>
    <h1>Choice Game</h1>
    <h2>{props.gameConfig.question}</h2>

    {
      props.gameConfig.status.gameOver
        ? <ChoiceGameAnswer myAnswer={getMyAnswer(props)} actualAnswer={props.gameConfig.answer} />
        : <ChoiceGameOptions options={props.gameConfig.options} playerAnswers={props.gameConfig.playerAnswers}
                             myPlayer={props.myPlayer} gameStatus={props.gameStatus} />

    }
  </div>

}
