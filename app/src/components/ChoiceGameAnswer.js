import React from "react";

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
        <h1>{"ANSWER: " + props.actualAnswer}</h1>
    }

    {
      correct &&
        <h1>{"You answered correctly in " + elapsedTime + " seconds and gained " + points + " points!"}</h1>
    }

    {
      incorrect &&
        <h1>You got it wrong :(</h1>
    }

    {
      done && noneAnswer &&
        <h1>You didn't even answer!?!</h1>
    }
  </div>

}
