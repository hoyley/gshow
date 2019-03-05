import React from 'react';
import './PlayerUpdate.css'

export default (props) => {

  let pillClass = " playerAnswer";

  if (props.answer && props.correct) {
    pillClass += " playerAnswerCorrect";
  } else if (props.answer) {
    pillClass += " playerAnswerIncorrect";
  }

  const points = props.points <= 0 ? props.points : "+" + props.points;
  const rootClass = props.className || "";

  return <div className={rootClass + pillClass}>
    <label className="updateName">{props.playerName}</label>
    {
      props.answer
        && <label className="updateAnswer">{ props.answer }</label>
    }
    {
      props.answer && props.correct
      && <label className="updateScore">{ points }</label>
    }
  </div>
};
