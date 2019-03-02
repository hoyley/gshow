import React from 'react';
import './PlayerUpdate.css'

export default (props) => {
  const answerClass = "updateAnswer " +
    (props.correct ? "" : " updateIncorrect");
  const points = props.points <= 0 ? props.points : "+" + props.points;

  const rootClass = props.className || "";

  return <div className={rootClass + " playerAnswer"}>
    <label className="updateName">{ props.playerName}</label>
    {
      props.answer && props.correct
        && <label className="updateScore">{ points }</label>
    }
    {
      props.answer
        && <label className={answerClass}>{ props.answer }</label>
    }
  </div>
};
