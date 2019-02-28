import React from "react";
import './ChoiceGameAnswer.css';

const answeredCorrectly = (answer) => {
  return answer && answer.correct;
};

const answeredIncorrectly = (answer) => {
  return answer && !answer.correct;
};

const noAnswer = (answer) => {
  return !answer;
}


export default (props) => {
  const correct = answeredCorrectly(props.myAnswer);
  const incorrect = answeredIncorrectly(props.myAnswer);
  const noneAnswer = noAnswer(props.myAnswer);
  const done = !!props.actualAnswer;
  const elapsedTime = props.myAnswer && props.myAnswer.timeElapsed;
  const points = props.myAnswer && props.myAnswer.points;

  return <div>
    {
      done &&
        <div className="answer">{props.actualAnswer}</div>
    }

    {
      correct &&
        <div className="answerMessage correct">{"You answered correctly in " + elapsedTime + " seconds and gained " + points + " points!"}</div>
    }

    {
      incorrect &&
        <div className="answerMessage incorrect">You got it wrong :(</div>
    }

    {
      done && noneAnswer &&
        <div className="answerMessage noAnswer">You didn't even answer!?!</div>
    }
  </div>

}
