import React from "react";
import ChoiceGameOptions from './ChoiceGameOptions';
import ChoiceGameAnswer from './ChoiceGameAnswer';
import './ChoiceGame.css'

const getMyAnswer = (props) => {
  return props.gameConfig.status.gameOver && props.myPlayer && props.gameConfig.playerAnswers.find(p => p.id === props.myPlayer.id);
};

export default (props) => {

  return <div>
    <h2 className="questionText">{props.gameConfig.question}</h2>

    {
      props.gameConfig.status.gameOver
        ? <ChoiceGameAnswer myAnswer={getMyAnswer(props)} actualAnswer={props.gameConfig.answer} />
        : <ChoiceGameOptions options={props.gameConfig.options} playerAnswers={props.gameConfig.playerAnswers}
                             myPlayer={props.myPlayer} gameStatus={props.gameStatus} />

    }
  </div>

}
