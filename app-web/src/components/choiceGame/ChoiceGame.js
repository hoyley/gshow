import React from "react";
import ChoiceGameOptions from './Options';
import ChoiceGameAnswer from './Answer';
import ChoiceGamePlayerUpdates from './PlayerUpdates';
import './ChoiceGame.css'

const getMyAnswer = (props) => {
  return props.gameConfig.status.gameOver
    && props.myPlayer
    && props.gameConfig.playerAnswers.find(p => p.id === props.myPlayer.id);
};

export default (props) => {

  return <div className="questionContainer">
    <h2 className="questionText">{props.gameConfig.question}</h2>
    {
      props.gameConfig.imagePath &&
        <img className="questionImage"
             src={"/images/" + props.gameConfig.imagePath}
             alt="Question" />
    }

    {
      props.gameConfig.status.gameOver
        ? <ChoiceGameAnswer myAnswer={getMyAnswer(props)}
                            actualAnswer={props.gameConfig.answer} />
        : <ChoiceGameOptions options={props.gameConfig.options}
                             playerAnswers={props.gameConfig.playerAnswers}
                             myPlayer={props.myPlayer}
                             gameStatus={props.gameStatus} />

    }

    {
      props.gameConfig.status.gameOver &&
        <ChoiceGamePlayerUpdates className="playerUpdatesContainer"
                                 players={props.players}
                                 playerAnswers={props.gameConfig.playerAnswers} />
    }

  </div>

}
