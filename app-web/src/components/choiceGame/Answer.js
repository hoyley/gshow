import React from "react";
import './Answer.css';

export default class extends React.Component {

  answeredCorrectly() {
    const answer = this.props.myAnswer;
    return answer && answer.correct;
  }

  answeredIncorrectly() {
    const answer = this.props.myAnswer;
    return answer && !!answer.answer && !answer.correct;
  }

  noAnswer() {
    return !this.props.myAnswer || !this.props.myAnswer.answer;
  }

  renderAnswer() {
    const actualAnswer = this.props.actualAnswer;
    const myAnswer = this.props.myAnswer && this.props.myAnswer.answer;

    return <div>
      {
        this.answeredCorrectly()
          ? <div className="answerText correctAnswer">{myAnswer}</div>
          : <div>
              <div className="answerText incorrectAnswer">{myAnswer}</div>
              <div className="answerText answer">{actualAnswer}</div>
            </div>
      }
    </div>
  }


  render() {
    const myAnswer = this.props.myAnswer;
    const correct = this.answeredCorrectly(myAnswer);
    const incorrect = this.answeredIncorrectly(myAnswer);
    const noneAnswer = this.noAnswer(myAnswer);
    const elapsedTime = myAnswer && myAnswer.timeElapsed;
    const points = myAnswer && myAnswer.points;

    return <div>
        {
          this.renderAnswer()
        }

        {
          correct &&
          <div
            className="answerMessage correct">{"You answered correctly in " + elapsedTime + " seconds and gained " + points + " points!"}</div>
        }

        {
          incorrect &&
          <div className="answerMessage incorrect">Incorrect</div>
        }

        {
          noneAnswer &&
          <div className="answerMessage noAnswer">You didn't answer in time</div>
        }
      </div>

  }

}
